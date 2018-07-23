package com.dem.websocketexec.core;

import com.dem.websocketexec.util.DevToolsParser;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URI;

public class WebSocketExecutor {

    private URI uri;
    private WebSocket ws;
    private final Object waitCoord = new Object();
    private final static int timeoutInSecs = 5;

    public WebSocketExecutor() {
    }

    public void executeString(WebDriver driver, String extensionId, String stringToExecute) {
        DevToolsParser devToolsParser = new DevToolsParser(driver);
        devToolsParser.parseDevToolsPort(devToolsParser.getDevToolLogEntry());
        String devToolsUri = devToolsParser.getUrl();
        SocketExtractor se = new SocketExtractor();
        setUri(se.getWebSocketUri(devToolsUri, extensionId));
        sendString(stringToExecute);
    }

    private void sendWSMessage(String url, String message) throws IOException, WebSocketException, InterruptedException, JSONException {
        JSONObject jsonObject = new JSONObject(message);
        final int messageId = jsonObject.getInt("id");
        if (ws == null) {
            ws = new WebSocketFactory()
                    .createSocket(url)
                    .addListener(new WebSocketAdapter() {
                        @Override
                        public void onTextMessage(WebSocket ws, String message) throws JSONException {
                            System.out.println(message);
                            if (new JSONObject(message).getString("method").equals("Network.requestIntercepted   ")) {
                                System.out.println("found");
                            }
                            // Received a response. Print the received message.
                            if (new JSONObject(message).getInt("id") == messageId) {
                                synchronized (waitCoord) {
                                    waitCoord.notifyAll();
                                }
                            }
                        }
                    })
                    .connect();
        }
        ws.sendText(message);
        synchronized (waitCoord) {
            waitCoord.wait(timeoutInSecs * 1000);
        }
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    private void sendString(String stringToExecute) {

        String req = "{ " +
                "\"id\": 1," +
                "\"method\": \"Runtime.evaluate\"," +
                "\"params\": {" +
                "\"expression\": \"" + stringToExecute + "\"" +
                "}}";

        try {
            this.sendWSMessage(this.uri.toString(), req);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ws.disconnect();

    }
}

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
    private final static int TIMEOUT_IN_SECS = 3;

    public Object executeString(WebDriver driver, String extensionId, String stringToExecute) {
        DevToolsParser devToolsParser = new DevToolsParser(driver);
        devToolsParser.parseDevToolsPort(devToolsParser.getDevToolLogEntry());
        String devToolsUri = devToolsParser.getUrl();
        SocketExtractor se = new SocketExtractor();
        setUri(se.getWebSocketUri(devToolsUri, extensionId));
        return sendString(stringToExecute);
    }

    private JSONObject sendWSMessage(String url, String message) throws IOException, WebSocketException, InterruptedException, JSONException {
        final JSONObject[] result = {null};
        if (ws == null) {
            ws = new WebSocketFactory()
                    .createSocket(url)
                    .addListener(new WebSocketAdapter() {
                        @Override
                        public void onTextMessage(WebSocket ws, String message) throws JSONException {
                            /*if (new JSONObject(message).getString("resu").equals("Network.requestIntercepted   ")) {
                                System.out.println("found");
                            }*/
                            // Received a response. Print the received message.
                            if (!new JSONObject(message).getString("result").isEmpty()) {
                                result[0] = new JSONObject(message);
                                synchronized (waitCoord) {
                                    waitCoord.notifyAll();
                                }
                            } else {
                                result[0] = new JSONObject(message);
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
            waitCoord.wait(TIMEOUT_IN_SECS * 1000);
        }
        return result[0];
    }

    private void sendWSMessage2(String url, String message) throws IOException, WebSocketException, InterruptedException, JSONException {
        if (ws == null) {
            ws = new WebSocketFactory()
                    .createSocket(url).connect().sendText(message).disconnect();
        }
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    private JSONObject sendString(String stringToExecute) {
        JSONObject result = null;
        try {
            result = this.sendWSMessage(this.uri.toString(), stringToExecute);
        } catch (IOException | WebSocketException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        ws.disconnect();
        return result;
    }
}

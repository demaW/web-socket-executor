package com.dem.websocketexec.core;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

public class WebSocketExecutor {

    private URI uri;
    private WebSocket ws;
    private final Object waitCoord = new Object();
    private final int timeoutInSecs = 5;

    public WebSocketExecutor(URI uri) {
        this.uri = uri;
    }

    public void executeString(String stringToExecute) {

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
}

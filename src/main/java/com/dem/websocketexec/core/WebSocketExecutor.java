package com.dem.websocketexec.core;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

public class WebSocketExecutor {

    private static final String RESULT = "result";
    private Logger log = Logger.getLogger(WebSocketExecutor.class);
    private URI uri;
    private WebSocket ws;
    private final Object waitCoord = new Object();
    private static final int TIMEOUT_IN_SECS = 3;

    public Object executeString(String devToolsUri, String extensionId, String stringToExecute) {
        SocketExtractor se = new SocketExtractor();
        setUri(se.getWebSocketUri(devToolsUri, extensionId));
        JSONObject result = null;
        try {
            result = sendString(stringToExecute);
        } catch (JSONException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    private JSONObject sendWSMessage(String url, String message) throws IOException, WebSocketException, InterruptedException {
        final JSONObject[] result = {null};
        log.info("Sending websocket command to URL: " + url);
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
                            if (!new JSONObject(message).getString(RESULT).isEmpty()) {
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


    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    private JSONObject sendString(String stringToExecute) throws JSONException {
        JSONObject result = null;
        try {
            result = this.sendWSMessage(this.uri.toString(), stringToExecute);
        } catch (IOException | WebSocketException | InterruptedException e) {
            log.error("Error during sending WS message\n", e);
            Thread.currentThread().interrupt();
        }
        ws.disconnect();
        assert result != null;
        if (checkIfNoErrors(result)) {
            return result;
        } else {
            throw new IllegalArgumentException(result.getJSONObject(RESULT).getJSONObject(RESULT).getString("description"));
        }
    }

    public boolean checkIfNoErrors(JSONObject jsonObject) {
        try {
            if (jsonObject.getJSONObject(RESULT).getJSONObject(RESULT).getString("subtype") != null)
                return !jsonObject.getJSONObject(RESULT).getJSONObject(RESULT).getString("subtype").equalsIgnoreCase("error");
        } catch (JSONException e) {
            log.info("No errors in JSON response. Proceeding...");
        }
        return true;
    }
}

package com.dem.websocketexec.core;


import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class SocketExtractor {

    private static final Logger log = Logger.getLogger(SocketExtractor.class);

    public URI getWebSocketUri(String debugUrl, String extensionID) {
        String webSocketDebuggerUrl = null;
        try {
            URL url = new URL(debugUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String json = org.apache.commons.io.IOUtils.toString(reader);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("url").equals(extensionID)) {
                    webSocketDebuggerUrl = jsonObject.getString("webSocketDebuggerUrl");
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error during parsing URI", e);
        } catch (JSONException e) {
            log.error("Error during parsing JSON", e);
        }

        if (webSocketDebuggerUrl == null){
            log.error("Websocket url not extracted");
            return null;
        }
        return URI.create(webSocketDebuggerUrl);
    }
}

package com.dem.websocketexec.core;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultParser {

    private static final Logger log = Logger.getLogger(ResultParser.class);
    private static final String RESULT = "result";

    private ResultParser() {
    }

    public static String getText(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(RESULT).getJSONObject(RESULT).getString("value");
        } catch (JSONException e) {
            log.error("Error during getting text\n", e);
        }
        return null;
    }
}

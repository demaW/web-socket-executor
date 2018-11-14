package com.dem.websocketexec.core;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultParser {

    private static final Logger log = Logger.getLogger(ResultParser.class);
    private static final String RESULT = "result";

    public static String getText(JSONObject jsonObject) {
        try {
            if (checkIfNoErrors(jsonObject)) {
                return jsonObject.getJSONObject(RESULT).getJSONObject(RESULT).getString("value");
            } else {
                log.error("Error appeared... please check your request");
                return null;
            }
        } catch (JSONException e) {
            log.error("Error during getting text\n", e);
        }
        return null;
    }


    public static boolean checkIfNoErrors(JSONObject jsonObject){
        // jsonObject
        try {
            if (jsonObject.getJSONObject(RESULT).getJSONObject(RESULT).getString("subtype") != null)
                return !jsonObject.getJSONObject(RESULT).getJSONObject(RESULT).getString("subtype").equalsIgnoreCase("error");
        } catch (JSONException e){
            log.error("Error during parsing for any error messages");
        }
        return true;
    }
}

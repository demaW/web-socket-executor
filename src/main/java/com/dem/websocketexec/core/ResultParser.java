package com.dem.websocketexec.core;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultParser {

    public static String getText(JSONObject jsonObject) {
        try {
            if (checkIfNoErrors(jsonObject)) {
                return jsonObject.getJSONObject("result").getJSONObject("result").getString("value");
            } else {
                System.err.println("Error appeared... please check your request");
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean checkIfNoErrors(JSONObject jsonObject) throws JSONException {
        // jsonObject
        try {
            if (jsonObject.getJSONObject("result").getJSONObject("result").getString("subtype") != null)
                return !jsonObject.getJSONObject("result").getJSONObject("result").getString("subtype").equalsIgnoreCase("error");
        } catch (JSONException e){
            System.out.println("Error not found");
        }
        return true;
    }
}

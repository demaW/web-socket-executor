package com.dem.websocketexec.core;

public class QueryProvider {

    private QueryProvider() {
    }

    public static String getRequest(String stringToExecute) {
        return "{ " +
                "\"id\": 1," +
                "\"method\": \"Runtime.evaluate\"," +
                "\"params\": {" +
                "\"expression\": \"" + stringToExecute + "\"" +
                "}}";
    }

    public static String getClickWithJQuerySelector(String selector) {
        String query = "document.querySelector(\\\"" + selector + "\\\").click()";
        return getRequest(query);
    }

    public static String getTextWithJQuerySelector(String selector) {
        String query = "document.querySelector(\\\"" + selector + "\\\").innerHTML";
        return getRequest(query);
    }

    public static String getAttributeWithJQuerySelector(String selector, String attribute) {
        String query = "document.querySelector(\\\"" + selector + "\\\").getAttribute(\\\"" + attribute + "\\\")";
        return getRequest(query);
    }

    public static void clearTextValue(){

    }

    public static void setTextValue(){

    }

    public static void sendKeyByJSEventCode(){

    }
}

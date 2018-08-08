package com.dem.websocketexec.core;

public class QueryProvider {

    private QueryProvider(){}

    public static String getRequest(String stringToExecute) {
        return "{ " +
                "\"id\": 1," +
                "\"method\": \"Runtime.evaluate\"," +
                "\"params\": {" +
                "\"expression\": \"" + stringToExecute + "\"" +
                "}}";
    }

    public static String getClickWithJQuerySelector(String selector) {
        String queryClick = "document.querySelector(\\\"" + selector + "\\\").click()";
        return getRequest(queryClick);
    }

    public static String getTextWithJQuerySelector(String selector) {
        String queryClick = "document.querySelector(\\\"" + selector + "\\\").innerHTML";
        return getRequest(queryClick);
    }

}

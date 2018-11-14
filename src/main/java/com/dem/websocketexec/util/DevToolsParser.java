package com.dem.websocketexec.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

public class DevToolsParser {

    private static final String DEV_TOOLS_URI = "DevTools HTTP Request: http://localhost:";
    private WebDriver driver;
    private String url;

    public DevToolsParser(WebDriver driver) {
        this.driver = driver;
    }

    public LogEntry getDevToolLogEntry(){
        return this.driver.manage().logs().get(LogType.DRIVER).getAll().stream().filter(logEntry -> logEntry.getMessage().contains(DEV_TOOLS_URI)).findFirst().get();
    }

    public void parseDevToolsPort(LogEntry logEntry){
        String message = logEntry.getMessage();
        int start = message.indexOf("http://");
        int end = message.indexOf("/version");
        message = message.substring(start, end);
        setUrl(message);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //TODO rewrite this code to check if no value then parse logs. When present, skip. Add function to update it by re-parsing;
}

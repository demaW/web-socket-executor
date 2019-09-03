package com.dem.websocketexec.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DevToolsParser {

    private WebDriver driver;

    public DevToolsParser(WebDriver driver) {
        this.driver = driver;
    }

    public String getDevToolsUri() {
        return parseDevToolsPort(((ChromeDriver) driver).getCapabilities().getCapability("goog:chromeOptions").toString());
    }

    public String parseDevToolsPort(String debuggerEntry) {
        String result = debuggerEntry.substring(1, debuggerEntry.length() - 1).substring(debuggerEntry.indexOf('='));
        return "http://" + result +"/json";
    }
}

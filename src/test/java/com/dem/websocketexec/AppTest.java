package com.dem.websocketexec;

import com.dem.websocketexec.core.QueryProvider;
import com.dem.websocketexec.core.ResultParser;
import com.dem.websocketexec.core.WebSocketExecutor;
import com.dem.websocketexec.util.DevToolsParser;
import org.json.JSONObject;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.logging.Level;

public class AppTest {
    private static final String EXTENSION_ID = "chrome-extension://iblgdcjagdifpikcobibfpkddkphllmc/popup/popup.html";
    private static final String DIV_ID_LOGIN_BUTTON = "div[id='login-button']";
    private static final String LOGIN_TEXT_BUTTON = "div[class='login__title']";
    private ChromeDriver driver;

    @Test
    public void test() {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");

        //add performance log
        ChromeOptions options = getChromeOptions();
        driver = new ChromeDriver(options);

        DevToolsParser devToolsParser = new DevToolsParser(driver);
        devToolsParser.parseDevToolsPort(devToolsParser.getDevToolLogEntry());
        String devToolsUri = devToolsParser.getUrl();

        WebSocketExecutor webSocketExecutor = new WebSocketExecutor();
        String stringToExecute = QueryProvider.getClickWithJQuerySelector(DIV_ID_LOGIN_BUTTON);
        webSocketExecutor.executeString(devToolsUri, EXTENSION_ID, stringToExecute);
        System.out.println();
        webSocketExecutor = new WebSocketExecutor();
        Object resultT = webSocketExecutor.executeString(devToolsUri, EXTENSION_ID, QueryProvider.getTextWithJQuerySelector(DIV_ID_LOGIN_BUTTON));
        String value = ResultParser.getText((JSONObject) resultT);
        System.out.println(value);
        webSocketExecutor = new WebSocketExecutor();
        resultT = webSocketExecutor.executeString(devToolsUri, EXTENSION_ID, QueryProvider.getTextWithJQuerySelector(LOGIN_TEXT_BUTTON));
        value = ResultParser.getText((JSONObject) resultT);
        System.out.println(value);
    }

    @AfterTest
    public void end() {
        driver.quit();
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        options.addExtensions(new File("driver/swa.crx"));
        return options;
    }

}

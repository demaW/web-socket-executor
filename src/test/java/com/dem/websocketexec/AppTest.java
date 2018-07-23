package com.dem.websocketexec;

import com.dem.websocketexec.core.WebSocketExecutor;
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
    public static final String EXTENSION_ID = "chrome-extension://iblgdcjagdifpikcobibfpkddkphllmc/popup/popup.html";
    ChromeDriver driver;

    @Test
    public void test() {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");

        //add performance log
        ChromeOptions options = getChromeOptions();
        driver = new ChromeDriver(options);

        WebSocketExecutor webSocketExecutor = new WebSocketExecutor();
        String stringToExecute = "document.querySelector(\\\"div[id='login-button']\\\").click()";
        webSocketExecutor.executeString(driver, EXTENSION_ID, stringToExecute);
        System.out.println();
        webSocketExecutor.executeString(driver, EXTENSION_ID, stringToExecute);

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

package com.dem.websocketexec;

import com.dem.websocketexec.core.SocketExtractor;
import com.dem.websocketexec.core.WebSocketExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URI;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppTest {
    ChromeDriver driver;

    @Test
    public void test() {
        int random = new Random().nextInt(10000) + 50000;
        ChromeOptions options = new ChromeOptions();
        options.addExtensions(new File("driver/swa.crx"));
        System.setProperty("webdriver.chrome.driver", "driver/2.exe");
        driver = new ChromeDriver(options);
        driver.get("chrome://version");
        String text = driver.findElement(By.id("command_line")).getText();
        SocketExtractor se = new SocketExtractor();
        URI webSocket = se.getWebSocketUri("http://localhost:" + extractPort(text) + "/json", "chrome-extension://iblgdcjagdifpikcobibfpkddkphllmc/popup/popup.html");
        WebSocketExecutor webSocketExecutor = new WebSocketExecutor(webSocket);
        webSocketExecutor.executeString("document.querySelector(\\\"div[id='login-button']\\\").click()");
        System.out.println();
    }

    @AfterTest
    public void end() {
        driver.quit();
    }

    public String extractPort(String mydata) {
        Pattern pattern = Pattern.compile("port=(\\d*) --t");
        Matcher matcher = pattern.matcher(mydata);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}

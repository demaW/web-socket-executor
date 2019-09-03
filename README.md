# web-socket-executor
This is utility for WebDriver

Set following options to be able to extract your debug port:
```java
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
```



To execute some script against drivers websocket
```java
        DevToolsParser devToolsParser = new DevToolsParser(driver);
        WebSocketExecutor webSocketExecutor = new WebSocketExecutor(); // Create new executor before sending command
        String stringToExecute = QueryProvider.getClickWithJQuerySelector(DIV_ID_LOGIN_BUTTON); // form your query with QueryProvider class
        webSocketExecutor.executeString(devToolsUri, EXTENSION_ID, stringToExecute); // execute your Query against web socket
```
//TODO try execute script in background html
//TODO try to get response from WebSocket
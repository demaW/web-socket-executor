# web-socket-executor
This is utility for WebDriver

Set following options to be able to extract your debug port:
```java
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
```
//TODO try execute script in background html
//TODO try to get response from WebSocket
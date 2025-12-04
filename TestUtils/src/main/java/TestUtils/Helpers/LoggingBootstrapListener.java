package TestUtils.Helpers;

import logging.Log;
import logging.LogbackConfigurator;
import logging.JulBridge;
import org.slf4j.Logger;
import org.testng.ITestNGListener;

public class LoggingBootstrapListener implements ITestNGListener {

    private static final Logger log = Log.get(LogbackConfigurator.class);

    static {
        LogbackConfigurator.initFromRuntime();
        JulBridge.init();
        log.debug("[LoggingBootstrapListener] static init");
    }
}

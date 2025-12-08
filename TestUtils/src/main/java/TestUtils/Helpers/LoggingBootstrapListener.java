package TestUtils.Helpers;

import logging.Log;
import logging.LogbackConfigurator;
import logging.JulBridge;
import org.slf4j.Logger;
import org.testng.ITestNGListener;

public class LoggingBootstrapListener implements ITestNGListener {

    static {
        LogbackConfigurator.initFromRuntime();
        JulBridge.init();
        Logger log = Log.get(LoggingBootstrapListener.class);
        log.debug("[LoggingBootstrapListener] static init completed");
    }
}

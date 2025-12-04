package logging;

import java.util.logging.LogManager;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

public final class JulBridge {

    private static final Logger log = Log.get(JulBridge.class);

    private JulBridge() {}

    public static void init() {
        log.trace("JulBridge установлен");
        LogbackConfigurator.initFromRuntime();
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}

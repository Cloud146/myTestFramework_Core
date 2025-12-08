package logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import config.RuntimeReader;
import org.slf4j.LoggerFactory;
import java.net.URL;

public final class LogbackConfigurator {

    private LogbackConfigurator() {}

    public static void initFromRuntime() {
        String configName = RuntimeReader.getString("logback_config");

        if (configName == null || configName.isBlank()) {
            throw new IllegalStateException("В runtime.yaml не найдена настройка 'logback_config'");
        }

        URL url = Thread.currentThread().getContextClassLoader().getResource(configName);

        if (url == null) {
            throw new IllegalStateException("Файл конфигурации логов не найден в ресурсах: " + configName);
        }

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(url);
        } catch (JoranException e) {
            throw new RuntimeException("Ошибка конфигурации Logback: " + url, e);
        }
    }
}

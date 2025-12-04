package logging;

import FileUtils.PathResolver;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import config.RuntimeReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.file.Path;

public final class LogbackConfigurator {

    private static final Logger log = Log.get(LogbackConfigurator.class);

    private LogbackConfigurator() {}

    public static void initFromRuntime() {
        String configPath = RuntimeReader.getString("logback_config");
        if (configPath == null || configPath.isBlank()) {
            throw new IllegalStateException("runtime.yaml: missing 'logback_config'");
        }

        Path resolved = PathResolver.resolve(configPath);
        File file = resolved.toFile();

        log.trace("[LogbackConfigurator] configPath из runtime.yaml: {}", configPath);
        log.trace("[LogbackConfigurator] FRAMEWORK_ROOT: {}", PathResolver.getFrameworkRoot());
        log.trace("[LogbackConfigurator] Резолвленный путь: {}", resolved.toAbsolutePath());
        log.trace("[LogbackConfigurator] Файл существует?: {}", file.exists());

        if (!file.exists()) {
            throw new IllegalStateException("Logback config not found: " + file.getAbsolutePath());
        }

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        try {
            configurator.doConfigure(file);
        } catch (JoranException e) {
            throw new RuntimeException("Failed to configure Logback from " + file, e);
        }
    }
}

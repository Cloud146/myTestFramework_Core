package FileUtils;

import logging.Log;
import org.slf4j.Logger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathResolver {

    private static final Logger log = Log.get(PathResolver.class);

    private PathResolver() {}

    /**
     * Пытается найти реальный путь к папке/файлу по строке из конфига.
     * Стратегия поиска:
     * 1. Абсолютный путь.
     * 2. Относительно src/test/resources (для удобства в IDE).
     * 3. Относительно user.dir (рабочей директории запуска).
     * 4. Внутри Classpath (target/classes).
     */
    public static Path resolve(String pathStr) {
        if (pathStr == null || pathStr.isBlank()) {
            throw new IllegalArgumentException("Путь не может быть пустым");
        }

        Path path = Paths.get(pathStr);

        if (path.isAbsolute() && Files.exists(path)) {
            return path;
        }

        Path inResources = Paths.get(System.getProperty("user.dir"))
                .resolve("src/test/resources")
                .resolve(pathStr);
        if (Files.exists(inResources)) {
            return inResources.toAbsolutePath().normalize();
        }

        Path relative = Paths.get(System.getProperty("user.dir")).resolve(pathStr);
        if (Files.exists(relative)) {
            return relative.toAbsolutePath().normalize();
        }

        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(pathStr);
        if (resourceUrl != null) {
            try {
                return Paths.get(resourceUrl.toURI());
            } catch (Exception e) {
                log.warn("Не удалось преобразовать URL ресурса в Path: {}", resourceUrl);
            }
        }

        log.warn("Путь '{}' не найден ни в src/test/resources, ни в user.dir, ни в classpath. Используем относительный.", pathStr);
        return relative;
    }
}

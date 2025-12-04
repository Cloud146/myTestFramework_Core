package FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathResolver {

    private static final Path FRAMEWORK_ROOT;

    static {
        // Определяем корень фреймворка: идём вверх от user.dir, пока не найдём pom.xml
        Path dir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        while (dir != null && !Files.exists(dir.resolve("runtime.yaml"))) {
            dir = dir.getParent();
        }
        if (dir == null) {
            throw new IllegalStateException("Не удалось найти корень фреймворка (runtime.yaml)");
        }
        FRAMEWORK_ROOT = dir;
    }

    private PathResolver() {}

    /**
     * Резолвит путь относительно корня фреймворка.
     * Если путь абсолютный — проверяет, что он находится внутри FRAMEWORK_ROOT.
     * Если относительный — добавляет его к FRAMEWORK_ROOT.
     */
    public static Path resolve(String relativeOrAbsolute) {
        Path path = Paths.get(relativeOrAbsolute);

        if (!path.isAbsolute()) {
            path = FRAMEWORK_ROOT.resolve(relativeOrAbsolute).normalize();
        } else {
            path = path.toAbsolutePath().normalize();
            if (!path.startsWith(FRAMEWORK_ROOT)) {
                throw new IllegalArgumentException(
                        "Путь выходит за пределы фреймворка: " + path +
                                " (корень: " + FRAMEWORK_ROOT + ")"
                );
            }
        }

        return path;
    }

    /**
     * Возвращает корень фреймворка (папка, где лежит pom.xml и runtime.yaml).
     */
    public static Path getFrameworkRoot() {
        return FRAMEWORK_ROOT;
    }
}

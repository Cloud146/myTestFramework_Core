package config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import java.io.InputStream;
import java.util.*;

/**
 * Утилитный класс для чтения глобального runtime.yaml и модульных YAML-конфигов.
 * При инициализации загружает runtime.yaml из корня проекта (родительская папка user.dir).
 * Предоставляет методы для получения строковых, числовых, булевых значений,
 * а также вложенных секций и списков.
 * Дополнительно умеет подгружать конфиги отдельных модулей (selenium.yaml, rest.yaml и т.д.)
 * из classpath.
 */
public final class RuntimeReader {

    private static final Map<String, Object> runtimeConfig = new HashMap<>();

    private static final String[] SEARCH_PATHS = {
            "configuration_files/runtime.yaml",
            "runtime.yaml"
    };

    static {
        loadRuntimeYaml();
    }

    private RuntimeReader() {}

    /**
     * Загружает глобальный runtime.yaml в карту runtimeConfig.
     * Если файл отсутствует или не удаётся прочитать, выбрасывает RuntimeException.
     */
    private static void loadRuntimeYaml() {
        for (String path : SEARCH_PATHS) {
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
                if (is != null) {
                    Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
                    Map<String, Object> loaded = yaml.load(is);
                    if (loaded != null) {
                        runtimeConfig.putAll(loaded);
                    }
                    return;
                }
            } catch (Exception e) {
                System.err.printf("[RuntimeReader] Error parsing YAML '%s': %s%n", path, e.getMessage());
            }
        }

        System.err.println("[RuntimeReader] Config 'runtime.yaml' not found in classpath. Searched paths: "
                + Arrays.toString(SEARCH_PATHS));
    }

    /**
     * Возвращает значение по ключу в виде строки.
     */
    public static String getString(String key) {
        Object value = runtimeConfig.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * Возвращает значение по ключу в виде int.
     */
    public static int getInt(String key) {
        Object value = runtimeConfig.get(key);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    /**
     * Возвращает значение по ключу в виде boolean.
     */
    public static boolean getBoolean(String key) {
        Object value = runtimeConfig.get(key);
        return value != null && Boolean.parseBoolean(value.toString());
    }

    /**
     * Возвращает вложенную секцию (Map) по ключу.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSection(String key) {
        Object value = runtimeConfig.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return Collections.emptyMap();
    }

    /**
     * Возвращает список строк по ключу.
     */
    @SuppressWarnings("unchecked")
    public static List<String> getList(String key) {
        Object value = runtimeConfig.get(key);
        if (value instanceof List) {
            return (List<String>) value;
        }
        return Collections.emptyList();
    }

    // --- Загрузка модульных конфигов ---

    /**
     * Загружает YAML-конфиг конкретного модуля из classpath.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getModuleConfig(String moduleName) {
        String resource = moduleName + ".yaml";
        try (InputStream is = RuntimeReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (is == null) {
                return Collections.emptyMap();
            }
            Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
            Map<String, Object> loaded = yaml.load(is);
            return loaded != null ? loaded : Collections.emptyMap();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки конфига модуля: " + moduleName, e);
        }
    }
}

package config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.File;
import java.io.FileInputStream;
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

    static {
        loadRuntimeYaml();
    }

    private RuntimeReader() {}

    /**
     * Загружает глобальный runtime.yaml в карту runtimeConfig.
     * Если файл отсутствует или не удаётся прочитать, выбрасывает RuntimeException.
     */
    private static void loadRuntimeYaml() {
        try {
            String rootDir = System.getProperty("user.dir");
            File file = new File(new File(rootDir).getParentFile(), "runtime.yaml");

            if (!file.exists()) {
                throw new RuntimeException("Файл runtime.yaml не найден: " + file.getAbsolutePath());
            }

            try (FileInputStream fis = new FileInputStream(file)) {
                Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
                Map<String, Object> loaded = yaml.load(fis);
                if (loaded != null) {
                    runtimeConfig.putAll(loaded);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки runtime.yaml", e);
        }
    }

    /**
     * Возвращает значение по ключу в виде строки.
     *
     * @param key имя ключа верхнего уровня в runtime.yaml
     * @return строковое значение или null, если ключ отсутствует
     */
    public static String getString(String key) {
        Object value = runtimeConfig.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * Возвращает значение по ключу в виде int.
     *
     * @param key имя ключа верхнего уровня в runtime.yaml
     * @return числовое значение или 0, если ключ отсутствует
     * @throws NumberFormatException если значение не может быть преобразовано в число
     */
    public static int getInt(String key) {
        Object value = runtimeConfig.get(key);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    /**
     * Возвращает значение по ключу в виде boolean.
     *
     * @param key имя ключа верхнего уровня в runtime.yaml
     * @return true, если значение равно "true" (регистр не важен), иначе false
     */
    public static boolean getBoolean(String key) {
        Object value = runtimeConfig.get(key);
        return value != null && Boolean.parseBoolean(value.toString());
    }

    /**
     * Возвращает вложенную секцию (Map) по ключу.
     * <p>
     * Например, если в YAML есть:
     * <pre>
     * timeouts:
     *   implicit: 5
     *   explicit: 15
     * </pre>
     * то вызов getSection("timeouts") вернёт Map {implicit=5, explicit=15}.
     *
     * @param key имя секции
     * @return Map с содержимым секции или пустая Map, если секция отсутствует
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
     * <p>
     * Например, если в YAML есть:
     * <pre>
     * modules:
     *   - selenium
     *   - rest
     * </pre>
     * то вызов getList("modules") вернёт List ["selenium", "rest"].
     *
     * @param key имя ключа
     * @return список строк или пустой список, если ключ отсутствует или не является списком
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
     * <p>
     * Ищет файл {moduleName}.yaml в resources.
     * Например, getModuleConfig("selenium") загрузит selenium.yaml.
     *
     * @param moduleName имя модуля (без расширения)
     * @return Map с настройками модуля или пустая Map, если файл не найден
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

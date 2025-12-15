package config;

import browsers.BrowserConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import java.io.InputStream;
import java.util.*;

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

    public static String getString(String key) {
        Object value = runtimeConfig.get(key);
        return value != null ? value.toString() : null;
    }

    public static int getInt(String key) {
        Object value = runtimeConfig.get(key);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    public static boolean getBoolean(String key) {
        Object value = runtimeConfig.get(key);
        return value != null && Boolean.parseBoolean(value.toString());
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSection(String key) {
        Object value = runtimeConfig.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getList(String key) {
        Object value = runtimeConfig.get(key);
        if (value instanceof List) {
            return (List<String>) value;
        }
        return Collections.emptyList();
    }

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

    public static BrowserConfig getBrowserConfig(String browserName) {
        String path = "configuration_files/browsers_configs/" + browserName + ".yaml";

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException("Файл конфигурации браузера не найден в ресурсах: " + path);
            }

            Yaml yaml = new Yaml();

            return yaml.loadAs(is, BrowserConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении или парсинге конфига браузера (" + path + "): " + e.getMessage(), e);
        }
    }
}

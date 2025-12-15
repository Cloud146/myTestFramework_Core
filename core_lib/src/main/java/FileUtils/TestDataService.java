package FileUtils;

import config.RuntimeReader;
import logging.Log;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

public final class TestDataService {

    private static final Logger log = Log.get(TestDataService.class);

    private static final String DEFAULT_TESTDATA_DIR = "src/test/resources/testData";
    private static final String KEY_SCENARIO = "Сценарий";
    private static final String KEY_DATA = "Тестовые данные";

    private static final ThreadLocal<String> CURRENT_SCENARIO = new ThreadLocal<>();

    private static volatile Map<String, Map<String, Object>> index = Collections.emptyMap();
    private static volatile boolean initialized = false;

    private TestDataService() {}

    public static synchronized void initIfNeeded() {
        if (initialized) return;
        String testDataDir = getTestDataDir();
        Path absolutePath = PathResolver.resolve(testDataDir);
        log.debug("Инициализация тестовых данных. Путь из конфига: '{}'. Абсолютный путь: '{}'", testDataDir, absolutePath);

        index = buildIndex(absolutePath); // Передаем absolutePath
        initialized = true;

        log.debug("Загружено сценариев с данными: {}", index.keySet());
    }

    public static void setCurrentScenario(String scenarioName) {
        if (scenarioName == null || scenarioName.isBlank()) {
            throw new IllegalArgumentException("scenarioName must not be null or blank");
        }
        String cleanName = scenarioName.trim();
        CURRENT_SCENARIO.set(cleanName);

        log.debug("Установлен контекст сценария: [{}]", cleanName);
    }

    public static void clearCurrentScenario() {
        CURRENT_SCENARIO.remove();
    }

    public static Object get(String key) {
        Map<String, Object> scenarioData = findScenarioDataOrThrow();

        if (!scenarioData.containsKey(key)) {
            throw new IllegalStateException(String.format(
                    "Ключ '%s' не найден в данных сценария '%s'. Доступные ключи: %s",
                    key, CURRENT_SCENARIO.get(), scenarioData.keySet()
            ));
        }

        return scenarioData.get(key);
    }

    private static String getTestDataDir() {
        String dir = RuntimeReader.getString("testData_dir");
        return (dir == null || dir.isBlank()) ? DEFAULT_TESTDATA_DIR : dir;
    }

    private static Map<String, Map<String, Object>> buildIndex(Path root) {
        Map<String, Map<String, Object>> out = new LinkedHashMap<>();

        if (!Files.exists(root)) {
            log.error("Папка с данными не существует по пути: {}", root.toAbsolutePath());
            return out;
        }
        if (!Files.isDirectory(root)) {
            log.error("Это не директория: {}", root.toAbsolutePath());
            return out;
        }

        try (var stream = Files.walk(root)) {
            stream.filter(p -> p.toString().endsWith(".yaml"))
                    .forEach(p -> processYamlFile(p.toFile(), out));
        } catch (Exception e) {
            throw new RuntimeException("Failed to build test data index from: " + root, e);
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    private static void processYamlFile(File file, Map<String, Map<String, Object>> out) {
        try (InputStream is = new FileInputStream(file)) {
            Object rootObj = new Yaml().load(is);

            if (rootObj instanceof List) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) rootObj;
                for (Map<String, Object> entry : list) {
                    parseEntry(entry, out);
                }
            }
            else if (rootObj instanceof Map) {
                parseEntry((Map<String, Object>) rootObj, out);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read YAML: " + file.getPath(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseEntry(Map<String, Object> entry, Map<String, Map<String, Object>> out) {
        Object scenarioObj = entry.get(KEY_SCENARIO);
        Object dataObj = entry.get(KEY_DATA);

        if (scenarioObj instanceof String && dataObj instanceof Map) {
            String scenarioName = ((String) scenarioObj).trim(); // Убираем пробелы из YAML
            out.put(scenarioName, (Map<String, Object>) dataObj);
        }
    }

    private static Map<String, Object> findScenarioDataOrThrow() {
        initIfNeeded();
        String name = CURRENT_SCENARIO.get();
        if (name == null) throw new IllegalStateException("Current scenario not set");

        Map<String, Object> data = index.get(name);
        if (data == null) {
            log.warn("Внимание: Для сценария [{}] не найдено тестовых данных в YAML файлах.", name);
            return Collections.emptyMap();
        }
        return data;
    }
}
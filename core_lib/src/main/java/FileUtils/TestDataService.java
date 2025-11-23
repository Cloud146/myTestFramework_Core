package FileUtils;

import config.RuntimeReader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

/**
 * TestDataService загружает YAML-файлы из каталога testData_dir и
 * предоставляет доступ к значениям по ключам на основе имени сценария/теста.
 *
 * Имя сценария приходит из:
 *  - Cucumber: Scenario.getName()
 *  - TestNG: @Test(description = "...") или @DisplayName (если используешь JUnit5)
 */
public final class TestDataService {

    private static final String DEFAULT_TESTDATA_DIR = "src/test/resources/testData";
    private static final String KEY_SCENARIO = "Сценарий";
    private static final String KEY_DATA = "Тестовые данные";
    private static final String KEY_TYPE = "тип";
    private static final String KEY_VALUE = "значение";

    private static final ThreadLocal<String> CURRENT_SCENARIO = new ThreadLocal<>();
    private static volatile Map<String, Map<String, Map<String, Object>>> index = Collections.emptyMap(); // scenarioName -> (key -> itemMap)
    private static volatile boolean initialized = false;

    private TestDataService() {}

    /**
     * Инициализирует индекс данных тестов, если ещё не был инициализирован.
     * Вызывает рекурсивный обход каталога testData_dir и строит сопоставление:
     * "Сценарий" -> карта ключей из "Тестовые данные".
     */
    public static synchronized void initIfNeeded() {
        if (initialized) return;
        String testDataDir = getTestDataDir();
        index = buildIndex(Paths.get(testDataDir));
        initialized = true;
    }

    /**
     * Устанавливает текущее имя сценария/теста для обращений к данным.
     *
     * @param scenarioName имя сценария (Cucumber) или описание теста (TestNG description)
     */
    public static void setCurrentScenario(String scenarioName) {
        if (scenarioName == null || scenarioName.isBlank()) {
            throw new IllegalArgumentException("scenarioName must not be null or blank");
        }
        CURRENT_SCENARIO.set(scenarioName);
    }

    /**
     * Сбрасывает текущее имя сценария (например, в teardown).
     */
    public static void clearCurrentScenario() {
        CURRENT_SCENARIO.remove();
    }

    /**
     * Возвращает значение поля "значение" для ключа из блока "Тестовые данные",
     * основываясь на текущем установленном имени сценария.
     *
     * Пример: value("DoubleClickMessage") -> "You have done a double click".
     *
     * @param key ключ внутри "Тестовые данные"
     * @return строковое значение или null, если ключ отсутствует
     */
    public static String value(String key) {
        Map<String, Map<String, Object>> map = findScenarioDataOrThrow();
        Map<String, Object> item = map.get(key);
        if (item == null) return null;
        Object v = item.get(KEY_VALUE);
        return v == null ? null : String.valueOf(v);
    }

    /**
     * Возвращает карту элемента данных для ключа (тип, значение), если нужно читать и тип.
     *
     * @param key ключ внутри "Тестовые данные"
     * @return неизменяемая карта с полями "тип" и "значение", или пустая карта, если ключ не найден
     */
    public static Map<String, Object> item(String key) {
        Map<String, Map<String, Object>> map = findScenarioDataOrThrow();
        Map<String, Object> item = map.getOrDefault(key, Collections.emptyMap());
        return Collections.unmodifiableMap(item);
    }

    // ---------------- internals ----------------

    private static String getTestDataDir() {
        // Берём из runtime.properties через твою утилиту
        String dir = RuntimeReader.getString("testData_dir");
        return (dir == null || dir.isBlank()) ? DEFAULT_TESTDATA_DIR : dir;
    }

//    private static Map<String, Map<String, Map<String, Object>>> buildIndex(Path root) {
//        Map<String, Map<String, Map<String, Object>>> out = new LinkedHashMap<>();
//        if (!Files.exists(root) || !Files.isDirectory(root)) return out;
//        try {
//            try (var stream = Files.walk(root)) {
//                stream.filter(p -> p.getFileName().toString().endsWith(".yaml"))
//                        .forEach(p -> {
//                            Map<String, Object> doc = loadYaml(p.toFile());
//                            Object scenarioObj = doc.get(KEY_SCENARIO);
//                            Object dataObj = doc.get(KEY_DATA);
//                            if (!(scenarioObj instanceof String) || !(dataObj instanceof Map)) return;
//                            @SuppressWarnings("unchecked")
//                            Map<String, Map<String, Object>> dataMap = (Map<String, Map<String, Object>>) dataObj;
//                            out.put((String) scenarioObj, dataMap);
//                        });
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to build test data index from: " + root, e);
//        }
//        return out;
//    }

    private static Map<String, Map<String, Map<String, Object>>> buildIndex(Path root) {
        Map<String, Map<String, Map<String, Object>>> out = new LinkedHashMap<>();
        if (!Files.exists(root) || !Files.isDirectory(root)) return out;
        try {
            try (var stream = Files.walk(root)) {
                stream.filter(p -> p.getFileName().toString().endsWith(".yaml"))
                        .forEach(p -> {
                            Object rootObj = loadYaml(p.toFile());

                            // --- NEW: поддержка списка сценариев
                            if (rootObj instanceof List) {
                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> list = (List<Map<String, Object>>) rootObj;
                                for (Map<String, Object> entry : list) {
                                    Object scenarioObj = entry.get(KEY_SCENARIO);
                                    Object dataObj = entry.get(KEY_DATA);
                                    if (scenarioObj instanceof String && dataObj instanceof Map) {
                                        @SuppressWarnings("unchecked")
                                        Map<String, Map<String, Object>> dataMap = (Map<String, Map<String, Object>>) dataObj;
                                        out.put((String) scenarioObj, dataMap);
                                    }
                                }
                            }
                            // --- OLD: поддержка одиночного сценария (оставляем для обратной совместимости)
                            else if (rootObj instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> doc = (Map<String, Object>) rootObj;
                                Object scenarioObj = doc.get(KEY_SCENARIO);
                                Object dataObj = doc.get(KEY_DATA);
                                if (scenarioObj instanceof String && dataObj instanceof Map) {
                                    @SuppressWarnings("unchecked")
                                    Map<String, Map<String, Object>> dataMap = (Map<String, Map<String, Object>>) dataObj;
                                    out.put((String) scenarioObj, dataMap);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to build test data index from: " + root, e);
        }
        return out;
    }

//    private static Map<String, Object> loadYaml(File file) {
//        try (InputStream is = new FileInputStream(file)) {
//            Object root = new Yaml().load(is);
//            if (!(root instanceof Map)) {
//                throw new IllegalStateException("YAML must be mapping at root: " + file.getPath());
//            }
//            @SuppressWarnings("unchecked")
//            Map<String, Object> m = (Map<String, Object>) root;
//            return m;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to read YAML: " + file.getPath(), e);
//        }
//    }

    private static Object loadYaml(File file) {
        try (InputStream is = new FileInputStream(file)) {
            return new Yaml().load(is); // --- NEW: возвращаем Object, не приводим сразу к Map
        } catch (Exception e) {
            throw new RuntimeException("Failed to read YAML: " + file.getPath(), e);
        }
    }

    private static Map<String, Map<String, Object>> findScenarioDataOrThrow() {
        initIfNeeded();
        String name = CURRENT_SCENARIO.get();
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Current scenario name is not set. Call TestDataService.setCurrentScenario(...) in setup.");
        }
        Map<String, Map<String, Object>> data = index.get(name);
        if (data == null) {
            throw new IllegalStateException("No test data found for scenario: " + name);
        }
        return data;
    }
}
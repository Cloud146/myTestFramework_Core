package TestUtils.CucumberUtils;

import config.RuntimeReader;
import io.cucumber.tagexpressions.TagExpressionParser;
import logging.Log;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CucumberRuntimeConfig {

    private static final String SECTION = "cucumber_settings";
    private static final String KEY_RUN_BY = "run_by";
    private static final String KEY_VALUE = "value";

    private final String runBy;
    private final List<String> values;

    private static final Logger log = Log.get(CucumberRuntimeConfig.class);

    private CucumberRuntimeConfig(String runBy, List<String> values) {
        this.runBy = runBy;
        this.values = values;
    }

    public static CucumberRuntimeConfig load() {
        Map<String, Object> config = RuntimeReader.getSection(SECTION);
        if (config.isEmpty()) {
            throw new RuntimeException("[CUCUMBER] В runtime.yaml отсутствует секция '" + SECTION + "'");
        }

        String runBy = readRunBy(config);
        List<String> values = readValues(config);

        return new CucumberRuntimeConfig(runBy, values);
    }

    // --- отдельный метод для чтения run_by
    private static String readRunBy(Map<String, Object> config) {
        Object raw = config.get(KEY_RUN_BY);
        if (!(raw instanceof String) || ((String) raw).isBlank()) {
            throw new RuntimeException("[CUCUMBER] Параметр 'run_by' пустой или отсутствует");
        }
        return ((String) raw).trim().toLowerCase();
    }

    // --- отдельный метод для чтения value
    private static String readValue(Map<String, Object> config) {
        Object raw = config.get(KEY_VALUE);
        if (!(raw instanceof String) || ((String) raw).isBlank()) {
            throw new RuntimeException("[CUCUMBER] Параметр 'value' пустой или отсутствует");
        }
        return ((String) raw).trim();
    }

    // -- парсим список значений через запятую
    private static List<String> readValues(Map<String, Object> config) {
        Object raw = config.get(KEY_VALUE);
        if (!(raw instanceof String) || ((String) raw).isBlank()) {
            throw new RuntimeException("[CUCUMBER] Параметр 'value' пустой или отсутствует");
        }
        return Arrays.stream(((String) raw).split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public void applySystemFilter() {
        switch (runBy) {
            case "tag":
                String tagExpr = String.join(" or ", values);
                try {
                    TagExpressionParser.parse(tagExpr);
                } catch (Exception e) {
                    throw new RuntimeException("[CUCUMBER] Некорректное выражение для фильтра по тегам: '" + tagExpr + "'", e);
                }
                System.setProperty("cucumber.filter.tags", tagExpr);
                log.debug("[CUCUMBER] Установлен фильтр по тегам: {}", tagExpr);
                break;

            case "scenario":
                String regex = values.stream()
                        .map(v -> v.startsWith("^") ? v : "^" + v + "$")
                        .collect(Collectors.joining("|"));
                System.setProperty("cucumber.filter.name", regex);
                log.debug("[CUCUMBER] Установлен фильтр по именам сценариев: {}", regex);
                break;

            default:
                throw new RuntimeException("[CUCUMBER] Некорректное значение run_by='" + runBy +
                        "'. Допустимые: tag или scenario");
        }
        log.info("[CUCUMBER] Запуск по {} = {}", runBy, String.join(", ", values));
    }

    public String getRunBy() {
        return runBy;
    }

    public List<String> getValues() {
        return values;
    }
}


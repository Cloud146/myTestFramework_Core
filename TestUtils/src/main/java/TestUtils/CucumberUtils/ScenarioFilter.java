package TestUtils.CucumberUtils;

import io.cucumber.testng.PickleWrapper;
import logging.Log;
import org.slf4j.Logger;
import java.util.*;
import java.util.stream.Collectors;

public final class ScenarioFilter {

    private static final Logger log = Log.get(ScenarioFilter.class);

    private ScenarioFilter() {}

    public static Object[][] filterScenarios(Object[][] scenarios, CucumberRuntimeConfig config) {
        log.debug("[CUCUMBER] Получено {} сценариев после применения фильтра", scenarios.length);

        for (Object[] scenario : scenarios) {
            if (!(scenario[0] instanceof PickleWrapper pw)) continue;
            String scenarioName = pw.getPickle().getName();
            List<String> tagNames = pw.getPickle().getTags().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            log.info("[CUCUMBER] Сценарий: {} | Теги: {}", scenarioName, String.join(", ", tagNames));
        }

        if (scenarios == null || scenarios.length == 0) {
            throw new RuntimeException("[CUCUMBER] Фильтр не совпал ни с одним сценарием.");
        }

        String runBy = config.getRunBy();
        List<String> values = config.getValues();

        Set<String> allScenarioNames = Arrays.stream(scenarios)
                .map(s -> ((PickleWrapper) s[0]).getPickle().getName())
                .collect(Collectors.toSet());

        Set<String> allTags = Arrays.stream(scenarios)
                .flatMap(s -> ((PickleWrapper) s[0]).getPickle().getTags().stream())
                .map(Object::toString)
                .collect(Collectors.toSet());

        List<String> missing = switch (runBy) {
            case "scenario" -> values.stream().filter(v -> !allScenarioNames.contains(v)).toList();
            case "tag" -> values.stream().filter(v -> !allTags.contains(v)).toList();
            default -> List.of();
        };

        if (!missing.isEmpty()) {
            log.warn("[CUCUMBER] Следующие {} не найдены среди доступных: {}", runBy, String.join(", ", missing));
        }

        boolean matchFound = Arrays.stream(scenarios).anyMatch(s -> {
            PickleWrapper pw = (PickleWrapper) s[0];
            String scenarioName = pw.getPickle().getName();
            List<String> tagNames = pw.getPickle().getTags().stream().map(Object::toString).toList();

            return switch (runBy) {
                case "scenario" -> values.stream().anyMatch(v -> scenarioName.equals(v));
                case "tag" -> values.stream().anyMatch(tagNames::contains);
                default -> false;
            };
        });

        if (!matchFound) {
            throw new RuntimeException("[CUCUMBER] Ни одно из значений '" + String.join(", ", values) + "' не совпало ни с одним " + runBy);
        }

        return scenarios;
    }
}

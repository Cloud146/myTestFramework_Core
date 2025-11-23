package tests.runner;

import TestUtils.CucumberUtils.CucumberRuntimeConfig;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import logging.Log;
import org.slf4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CucumberOptions(
        features = "classpath:features",
        glue = {
                "steps",   // шаги из core_lib
                "TestUtils.CucumberUtils",      // хуки и BaseTest
                "TestUtils.Assertion"
        },
        monochrome = true
)
public class CucumberRunnerTest extends AbstractTestNGCucumberTests {

    private static final Logger log = Log.get(CucumberRunnerTest.class);
    private static CucumberRuntimeConfig config;

    @BeforeSuite(alwaysRun = true)
    public void initConfig() {
        config = CucumberRuntimeConfig.load();
        config.applySystemFilter();
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        Object[][] scenarios = super.scenarios();
        log.debug("[CUCUMBER] Получено {} сценариев после применения фильтра", scenarios.length);

        for (Object[] scenario : scenarios) {
            Object pickleWrapper = scenario[0];

            if (!(pickleWrapper instanceof io.cucumber.testng.PickleWrapper)) continue;

            io.cucumber.testng.PickleWrapper pw = (io.cucumber.testng.PickleWrapper) pickleWrapper;
            String scenarioName = pw.getPickle().getName();
            List<String> tagNames = pw.getPickle().getTags().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());

            log.debug("[CUCUMBER] Сценарий: {} | Теги: {}", scenarioName, String.join(", ", tagNames));
        }

        if (scenarios == null || scenarios.length == 0) {
            throw new RuntimeException("[CUCUMBER] Фильтр не совпал ни с одним сценарием.");
        }

        String runBy = config.getRunBy();
        List<String> values = config.getValues();

        Set<String> allScenarioNames = Arrays.stream(scenarios)
                .map(s -> ((io.cucumber.testng.PickleWrapper) s[0]).getPickle().getName())
                .collect(Collectors.toSet());

        Set<String> allTags = Arrays.stream(scenarios)
                .flatMap(s -> ((io.cucumber.testng.PickleWrapper) s[0]).getPickle().getTags().stream())
                .map(Object::toString)
                .collect(Collectors.toSet());

        List<String> missing = switch (runBy) {
            case "scenario" -> values.stream()
                    .filter(v -> !allScenarioNames.contains(v))
                    .toList();
            case "tag" -> values.stream()
                    .filter(v -> !allTags.contains(v))
                    .toList();
            default -> List.of();
        };

        if (!missing.isEmpty()) {
            log.warn("[CUCUMBER] Следующие {} не найдены среди доступных: {}", runBy, String.join(", ", missing));
        }

        boolean matchFound = Arrays.stream(scenarios).anyMatch(s -> {
            io.cucumber.testng.PickleWrapper pw = (io.cucumber.testng.PickleWrapper) s[0];
            String scenarioName = pw.getPickle().getName();
            List<String> tagNames = pw.getPickle().getTags().stream()
                    .map(Object::toString)
                    .toList();

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

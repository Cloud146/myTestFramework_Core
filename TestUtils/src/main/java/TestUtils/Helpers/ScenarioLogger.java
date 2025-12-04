package TestUtils.Helpers;

import FileUtils.TestDataService;
import logging.Log;
import org.slf4j.Logger;
import java.lang.reflect.Method;

public final class ScenarioLogger {

    private static final Logger log = Log.get(ScenarioLogger.class);

    private ScenarioLogger() {}

    public static String extractDescription(Method m) {
        org.testng.annotations.Test testAnno = m.getAnnotation(org.testng.annotations.Test.class);
        if (testAnno != null && testAnno.description() != null && !testAnno.description().isBlank()) {
            return testAnno.description().trim();
        }
        return m.getDeclaringClass().getSimpleName() + "." + m.getName();
    }

    public static void logScenario(Method testMethod) {
        String scenarioName = extractDescription(testMethod);
        log.info("[TESTNG] Сценарий: {}", scenarioName);
        TestDataService.setCurrentScenario(scenarioName);
    }
}


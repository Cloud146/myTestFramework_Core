package TestUtils.CucumberUtils;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.util.ResultsUtils;
import java.util.UUID;

public class CucumberAllureScenarioLifecycle {

    private static final ThreadLocal<String> currentUuid = new ThreadLocal<>();

    /**
     * Стартует новый тест-кейс в Allure для текущего сценария.
     */
    public static void startScenario(Scenario scenario) {
        String uuid = UUID.randomUUID().toString();
        currentUuid.set(uuid);

        TestResult result = new TestResult();
        result.setUuid(uuid);
        result.setName(scenario.getName());
        result.setFullName(scenario.getId());
        result.setHistoryId(scenario.getId());

        scenario.getSourceTagNames()
                .forEach(tag -> result.getLabels().add(ResultsUtils.createLabel("tag", tag)));

        Allure.getLifecycle().scheduleTestCase(result);
        Allure.getLifecycle().startTestCase(uuid);
    }

    /**
     * Завершает тест-кейс в Allure, проставляя статус по результату сценария.
     */
    public static void stopScenario(Scenario scenario) {
        String uuid = currentUuid.get();
        if (uuid == null) return;

        Status status = scenario.isFailed() ? Status.FAILED : Status.PASSED;
        Allure.getLifecycle().updateTestCase(uuid, tr -> tr.setStatus(status));
        Allure.getLifecycle().stopTestCase(uuid);
        Allure.getLifecycle().writeTestCase(uuid);

        currentUuid.remove();
    }
}

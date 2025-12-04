package TestUtils.CucumberUtils;

import FileUtils.TestDataService;
import TestUtils.BaseTest.BaseTest;
import config.RuntimeReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import java.util.Map;

public class CucumberHooks extends BaseTest {

    @Before(order = 0)
    public void cucumberSetUp(Scenario scenario) {
        CucumberAllureScenarioLifecycle.startScenario(scenario);
        Allure.label("tag", "Cucumber_Test");

        TestDataService.initIfNeeded();
        TestDataService.setCurrentScenario(scenario.getName());

        Map<String, Object> retry = RuntimeReader.getSection("retry");
        retryCount = Integer.parseInt(retry.getOrDefault("count", 1).toString());

        setUp();
    }

    @After(order = 0)
    public void cucumberTearDown(Scenario scenario) {
        try {
            tearDown();
        } finally {
            CucumberAllureScenarioLifecycle.stopScenario(scenario);
        }
    }
}

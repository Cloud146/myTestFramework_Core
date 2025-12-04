package runner;

import TestUtils.CucumberUtils.CucumberConfigInitializer;
import TestUtils.CucumberUtils.CucumberRuntimeConfig;
import TestUtils.CucumberUtils.ScenarioFilter;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "classpath:features",
//        features = "Workspace/src/test/resources/features",
        glue = {
                "steps",   // шаги из core_lib
                "TestUtils.CucumberUtils",      // хуки и BaseTest
                "TestUtils.Assertion"
        },
        monochrome = true
)
public class CucumberRunnerTest extends AbstractTestNGCucumberTests {

    private static CucumberRuntimeConfig config;

    @BeforeSuite(alwaysRun = true)
    public void initConfig() {
        config = CucumberConfigInitializer.initAndFilter();
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        Object[][] scenarios = super.scenarios();
        return ScenarioFilter.filterScenarios(scenarios, config);
    }
}

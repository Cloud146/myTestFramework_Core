package TestUtils.CucumberUtils;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

@CucumberOptions()
public abstract class BaseCucumberRunner extends AbstractTestNGCucumberTests {

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
package TestUtils.Helpers;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AutoTagListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Object instance = result.getInstance();

        boolean isCucumber = instance instanceof AbstractTestNGCucumberTests;

        if (!isCucumber) {
            Allure.label("tag", "JavaIDE_Test");
        } else {
        }
    }
}

package TestUtils.ScreenshotUtil;

import logging.Log;
import org.slf4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class ScreenshotListener implements ITestListener {

    private static final Logger log = Log.get(ScreenshotListener.class);

    public ScreenshotListener() {
        log.debug("ScreenshotListener loaded");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.debug("ScreenshotListener.onTestFailure: {}", result == null ? "null" : result.getMethod());
        boolean attached = ScreenshotUtil.takeScreenshotOnFailure(result);
        if (attached) {
            log.debug("Screenshot attached for {}", result == null ? "unknown" : result.getMethod().getMethodName());
        } else {
            log.warn("Could not add screenshot attachment for {}", result == null ? "unknown" : result.getMethod().getMethodName());
        }
    }

    // остальные методы интерфейса можно оставить пустыми
    @Override public void onTestStart(ITestResult result) { }
    @Override public void onTestSuccess(ITestResult result) { }
    @Override public void onTestSkipped(ITestResult result) { }
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }
    @Override public void onStart(org.testng.ITestContext context) { }
    @Override public void onFinish(org.testng.ITestContext context) { }
}

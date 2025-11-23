package TestUtils.RetryUtil;

import config.RuntimeReader;
import io.qameta.allure.Allure;
import logging.Log;
import org.slf4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


import java.util.Map;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int attempt = 0;
    private final int maxRetries;

    private static final Logger log = Log.get(RetryAnalyzer.class);

    public RetryAnalyzer() {
        Map<String, Object> retryConfig = RuntimeReader.getSection("retry");
        this.maxRetries = Integer.parseInt(
                retryConfig.getOrDefault("count", 0).toString()
        );
    }

    @Override
    public boolean retry(ITestResult result) {
        if (attempt < maxRetries) {
            attempt++;
            String message = String.format(
                    "Перезапуск #%d из %d для теста %s",
                    attempt, maxRetries, result.getMethod().getMethodName()
            );
//            System.out.println("[RETRY] " + message);
            log.info(message);
            Allure.step(message); // шаг в отчёте
            return true;
        }
        return false;
    }
}

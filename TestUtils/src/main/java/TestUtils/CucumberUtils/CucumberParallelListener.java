package TestUtils.CucumberUtils;

import logging.Log;
import org.slf4j.Logger;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;
import java.util.List;

public class CucumberParallelListener implements IAlterSuiteListener {

    private static final Logger log = Log.get(CucumberParallelListener.class);

    @Override
    public void alter(List<XmlSuite> suites) {
        try {
            CucumberRuntimeConfig config = CucumberRuntimeConfig.load();

            int threadCount;

            if (config.isParallel()) {
                threadCount = config.getThreads();
                log.debug("[CUCUMBER] Включен ПАРАЛЛЕЛЬНЫЙ режим. Потоков: {}", threadCount);
            } else {
                threadCount = 1;
                log.debug("[CUCUMBER] Параллельный режим ОТКЛЮЧЕН (потоков: 1)");
            }

            for (XmlSuite suite : suites) {
                suite.setDataProviderThreadCount(threadCount);
            }

        } catch (Exception e) {
            log.warn("[CUCUMBER] Не удалось применить настройки параллельности из runtime.yaml: {}", e.getMessage());
        }
    }
}

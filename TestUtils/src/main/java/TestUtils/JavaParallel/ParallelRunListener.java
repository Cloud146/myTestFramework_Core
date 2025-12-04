package TestUtils.JavaParallel;

import logging.Log;
import org.slf4j.Logger;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import java.util.List;

/**
 * Слушатель, который включается до старта тестов.
 * Если находит класс с аннотацией @ParallelClass, принудительно включает параллелизм.
 */
public class ParallelRunListener implements IAlterSuiteListener {

    private static final Logger log = Log.get(ParallelRunListener.class);

    @Override
    public void alter(List<XmlSuite> suites) {
        for (XmlSuite suite : suites) {
            for (XmlTest xmlTest : suite.getTests()) {

                boolean enableParallel = false;
                int maxThreads = 0;

                for (XmlClass xmlClass : xmlTest.getClasses()) {
                    Class<?> realClass = xmlClass.getSupportClass();
                    if (realClass != null && realClass.isAnnotationPresent(ParallelClass.class)) {
                        enableParallel = true;
                        ParallelClass annotation = realClass.getAnnotation(ParallelClass.class);
                        maxThreads = Math.max(maxThreads, annotation.threads());
                    }
                }

                if (enableParallel) {
                    xmlTest.setParallel(XmlSuite.ParallelMode.METHODS);

                    if (maxThreads > 0) {
                        xmlTest.setThreadCount(maxThreads);
                        suite.setDataProviderThreadCount(maxThreads);
                    }

                    log.info("[ParallelRunListener] Включен параллельный запуск для: {} (Потоков: {})",
                            xmlTest.getName(),
                            (maxThreads > 0 ? maxThreads : "AUTO"));
                }
            }
        }
    }
}

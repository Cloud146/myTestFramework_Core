package TestUtils.RetryUtil;

import logging.Log;
import org.slf4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer {

    private static final Logger log = Log.get(RetryListener.class);

    public RetryListener() {
        log.debug("RetryListener loaded");
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        log.debug("transform called for: {}", testMethod);
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}

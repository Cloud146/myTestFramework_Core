package TestUtils.BaseTest;

import CurrentPageUtils.PageContext;
import FileUtils.TestDataService;
import TestUtils.Helpers.AdapterManager;
import TestUtils.Helpers.FrameworkDataInitializer;
import TestUtils.Helpers.ScenarioLogger;
import TestUtils.Assertion.AssertUtil;
import config.RuntimeReader;
import driverAdapter.AdapterHolder;
import driverAdapter.DriverAdapter;
import FileUtils.PageReader;
import io.qameta.allure.Step;
import logging.Log;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import steps.interactions.MouseSteps;
import steps.navigation.NavigationSteps;
import java.lang.reflect.Method;

/**
 * Базовый класс для всех тестов (UI, API, Mobile и т.д.).
 *
 * Отвечает за инициализацию данных и чтение глобальных настроек из runtime.yaml.
 * Если в конфиге указан ui_engine, то поднимается соответствующий адаптер.
 * Если ui_engine отсутствует, тесты работают без драйвера (например, REST).
 */
public abstract class BaseTest {

    protected DriverAdapter adapter;
    protected PageReader page;
    protected int retryCount;
    private static final Logger log = Log.get(BaseTest.class);

    protected AssertUtil assertUtil;

    // Шаги
    protected MouseSteps mouseSteps;
    protected NavigationSteps navigationSteps;

    @Step("Чтение тестовых данных и настроек фреймворка")
    @BeforeClass
    public void initData() {
        FrameworkDataInitializer.initTestData();
        retryCount = FrameworkDataInitializer.initRetryCount();
    }

    @BeforeMethod
    public void scenarioName(Method testMethod) {
        ScenarioLogger.logScenario(testMethod);
    }

    @BeforeMethod
    public void setUp() {
        String engine = RuntimeReader.getString("ui_engine");

        if (engine != null && !engine.isBlank()) {
            log.debug("Выбранный движок для ui: {}", engine);

            DriverAdapter localAdapter = AdapterManager.createAdapter(engine);

            AdapterHolder.set(localAdapter);

            this.adapter = localAdapter;

            assertUtil = new AssertUtil(localAdapter);
            mouseSteps = new MouseSteps(localAdapter);
            navigationSteps = new NavigationSteps(localAdapter);
        } else {
            this.adapter = null;
        }
        this.page = null;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        long threadId = Thread.currentThread().getId();
        log.debug("[Thread-{}] >>> Начало tearDown. Закрываем адаптер...", threadId);

        try {
            DriverAdapter currentAdapter = AdapterHolder.get();
            AdapterManager.closeAdapter(currentAdapter);
            log.debug("[Thread-{}] <<< Адаптер успешно закрыт.", threadId);
        } catch (Exception e) {
            log.warn("[Thread-{}] Ошибка при закрытии: {}", threadId, e.getMessage());
        } finally {
            AdapterHolder.clear();
            TestDataService.clearCurrentScenario();
            PageContext.getHolder().remove();
        }
    }
}
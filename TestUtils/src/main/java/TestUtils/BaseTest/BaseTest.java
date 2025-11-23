package TestUtils.BaseTest;

import FileUtils.TestDataService;
import TestUtils.Assertion.AssertUtil;
import logging.JulBridge;
import config.RuntimeReader;
import driverAdapter.AdapterHolder;
import driverAdapter.DriverAdapter;
import driverAdapter.DriverAdapterFactory;
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
import java.util.Map;
import java.util.ServiceLoader;

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
    protected Map<String, Object> timeouts;
    protected int retryCount;
    private static final Logger log = Log.get(BaseTest.class);

    protected AssertUtil assertUtil;

    // Шаги
    protected MouseSteps mouseSteps;
    protected NavigationSteps navigationSteps;

    @Step("Чтение тестовых данных и настроек фреймворка")
    @BeforeClass
    public void initData() {
        // Чтение тестовых данных (например, pages/*.yaml)
        TestDataService.initIfNeeded();

        // Читаем глобальные секции из runtime.yaml
        timeouts = RuntimeReader.getSection("timeouts");
        Map<String, Object> retry = RuntimeReader.getSection("retry");
        retryCount = Integer.parseInt(retry.getOrDefault("count", 1).toString());
    }

    @BeforeMethod
    public void scenarioName(Method testMethod) {
        String scenarioName = extractDescription(testMethod);
        log.info("[TESTNG] Сценарий: {}", scenarioName);
        TestDataService.setCurrentScenario(scenarioName);
    }

    @Step("Настройка тестового адаптера")
    @BeforeMethod
    public void setUp() {
        JulBridge.init();
        String engine = RuntimeReader.getString("ui_engine");

        if (engine != null && !engine.isBlank()) {
            log.debug("Выбранный движок для ui: {}", engine);
            adapter = ServiceLoader.load(DriverAdapterFactory.class).stream()
                    .map(ServiceLoader.Provider::get)
                    .filter(f -> f.getEngineName().equalsIgnoreCase(engine))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No adapter found for engine: " + engine))
                    .create();

            AdapterHolder.set(adapter); // кладём в holder

            assertUtil = new AssertUtil(adapter);

            // Инициализация шагов поверх адаптера
            mouseSteps = new MouseSteps(adapter);
            navigationSteps = new NavigationSteps(adapter);
        } else {
            adapter = null; // значит это не UI‑тест
        }

        // finder не создаём здесь — он будет создан в тесте через openPage("Page Name")
        page = null;
    }

    @Step("Закрытие тестового адаптера")
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (adapter != null) {
            adapter.close();
        }
        AdapterHolder.clear(); // очищаем
        TestDataService.clearCurrentScenario();
    }

    private String extractDescription(Method m) {
        org.testng.annotations.Test testAnno = m.getAnnotation(org.testng.annotations.Test.class);
        if (testAnno != null && testAnno.description() != null && !testAnno.description().isBlank()) {
            return testAnno.description().trim();
        }
        // Фолбэк: имя метода, если description не заполнено
        return m.getDeclaringClass().getSimpleName() + "." + m.getName();
    }
}
package steps.interactions;

import ErrorUtils.ExceptionThrow;
import ErrorUtils.FrameworkException;
import driverAdapter.AdapterHolder;
import driverAdapter.adapter_api_contracts.interaction.MouseAdapter;
import io.cucumber.java.bg.И;
import io.qameta.allure.Step;
import logging.Log;
import org.slf4j.Logger;

/**
 * Шаги для действий мышью.
 * Работают поверх MouseAdapter, чтобы в Allure были красивые шаги,
 * а реализация выбиралась в рантайме (Selenium, Playwright, Appium).
 */
public class MouseSteps {

    private final MouseAdapter mouseAdapter;
    private static final Logger log = Log.get(MouseSteps.class);

    public MouseSteps() {
        this.mouseAdapter = AdapterHolder.get();
    }

    public MouseSteps(MouseAdapter mouseAdapter) {
        this.mouseAdapter = mouseAdapter;
    }

    @И("Клик по элементу «(.+)»$")
    @Step("Клик по элементу: {elementName}")
    public void click(String elementName) throws FrameworkException{
        ExceptionThrow.elementActionException("Двойной клик", elementName, () -> {
            mouseAdapter.clickElement(elementName);
            log.info("Выполнен клик по элементу: {}", elementName);
        });
    }

    @И("Двойной клик по элементу «(.+)»$")
    @Step("Двойной клик по элементу: {elementName}")
    public void doubleClick(String elementName) throws FrameworkException{
        ExceptionThrow.elementActionException("Двойной клик", elementName, () -> {
            mouseAdapter.doubleClickElement(elementName);
            log.info("Выполнен двойной клик по элементу: {}", elementName);
        });
    }

    @И("Правый клик по элементу «(.+)»$")
    @Step("Правый клик по элементу: {elementName}")
    public void rightClick(String elementName) throws FrameworkException{
        ExceptionThrow.elementActionException("Двойной клик", elementName, () -> {
            mouseAdapter.rightClickElement(elementName);
            log.info("Выполнен правый клик по элементу: {}", elementName);
        });
    }
}
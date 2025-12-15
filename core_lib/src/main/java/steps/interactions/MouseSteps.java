package steps.interactions;

import ErrorUtils.ExceptionThrow;
import ErrorUtils.FrameworkException;
import driverAdapter.AdapterHolder;
import driverAdapter.adapter_api_contracts.interaction.MouseAdapter;
import io.cucumber.java.bg.И;
import io.qameta.allure.Step;
import logging.Log;
import org.slf4j.Logger;

public class MouseSteps {

    private static final Logger log = Log.get(MouseSteps.class);

    public MouseSteps() {
    }

    public MouseSteps(MouseAdapter ignored) {}

    private MouseAdapter getAdapter() {
        return AdapterHolder.get();
    }

    @И("Клик по элементу «(.+)»$")
    @Step("Клик по элементу: {elementName}")
    public void click(String elementName) throws FrameworkException{
        ExceptionThrow.elementActionException("Клик", elementName, () -> {
            getAdapter().clickElement(elementName);
            log.info("Выполнен клик по элементу: {}", elementName);
        });
    }

    @И("Двойной клик по элементу «(.+)»$")
    @Step("Двойной клик по элементу: {elementName}")
    public void doubleClick(String elementName) throws FrameworkException{
        ExceptionThrow.elementActionException("Двойной клик", elementName, () -> {
            getAdapter().doubleClickElement(elementName);
            log.info("Выполнен двойной клик по элементу: {}", elementName);
        });
    }

    @И("Правый клик по элементу «(.+)»$")
    @Step("Правый клик по элементу: {elementName}")
    public void rightClick(String elementName) throws FrameworkException{
        ExceptionThrow.elementActionException("Правый клик", elementName, () -> {
            getAdapter().rightClickElement(elementName);
            log.info("Выполнен правый клик по элементу: {}", elementName);
        });
    }
}
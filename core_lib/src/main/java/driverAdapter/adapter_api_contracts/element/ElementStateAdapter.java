package driverAdapter.adapter_api_contracts.element;

/**
 * Контракт получения состояний элементов
 */
public interface ElementStateAdapter {

    /** Элемент присутствует в DOM и виден пользователю? */
    boolean isElementDisplayed(String elementName);

    /** Элемент присутствует в DOM (неважно, виден или нет)? */
    boolean isElementPresent(String elementName);

    /** Элемент активен (enabled) для взаимодействия? */
    boolean isElementEnabled(String elementName);
}

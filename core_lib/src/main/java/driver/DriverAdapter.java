package driver;

import locator.Locator;

import java.util.List;

/**
 * Адаптер драйвера: поиск элементов по абстрактному Locator.
 * Конкретные реализации: SeleniumDriverAdapter, AppiumDriverAdapter, PlaywrightDriverAdapter...
 */
public interface DriverAdapter {
    /** Найти первый подходящий элемент. */
    ElementHandle find(Locator locator);

    /** Найти все подходящие элементы. */
    List<ElementHandle> findAll(Locator locator);

    // Можно добавить ожидания, таймауты, общий sleep/polling API и т.д.
}

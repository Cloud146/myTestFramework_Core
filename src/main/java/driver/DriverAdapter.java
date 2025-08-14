package driver;

import locator.Locator;

import java.util.List;

/**
 * Движок/провайдер: умеет искать элементы по абстрактному Locator.
 * Конкретные реализации: SeleniumDriverAdapter, AppiumDriverAdapter, PlaywrightDriverAdapter...
 */
public interface DriverAdapter {
    ElementHandle find(Locator locator);
    List<ElementHandle> findAll(Locator locator);

    // Можно добавить ожидания, таймауты, общий sleep/polling API и т.д.
}

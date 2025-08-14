package element;

import driver.DriverAdapter;
import driver.ElementHandle;
import locator.Locator;

import java.util.Objects;

/**
 * Обёртка над Locator + семантический тип элемента (button, input...)
 * Достаёт элемент по запросу через DriverAdapter на каждое действие (fresh lookup).
 */
public class UIElement {
    private final DriverAdapter driver;
    private final Locator locator;
    private final String semanticType;

    public UIElement(DriverAdapter driver, Locator locator, String semanticType) {
        this.driver = Objects.requireNonNull(driver);
        this.locator = Objects.requireNonNull(locator);
        this.semanticType = semanticType;
    }

    private ElementHandle handle() { return driver.find(locator); }

    public void click() { handle().click(); }

    public void type(String text) {
        // Базовая логика для input-подобных
        ElementHandle el = handle();
        el.clear();
        el.sendKeys(text);
    }

    public String text() { return handle().getText(); }

    public boolean visible() { return handle().isDisplayed(); }

    public String attr(String name) { return handle().getAttribute(name); }

    public String typeName() { return semanticType; }

    @Override public String toString() {
        return "UIElement{" + semanticType + " @ " + locator + "}";
    }
}

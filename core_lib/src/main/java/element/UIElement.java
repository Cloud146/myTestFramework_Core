package element;

import driver.DriverAdapter;
import driver.ElementHandle;
import locator.Locator;

import java.util.Objects;

/**
 * Высокоуровневая обёртка над Locator + семантическим типом (button, input...).
 * Каждый вызов получает актуальный ElementHandle через DriverAdapter (fresh lookup).
 */
public class UIElement {
    private final DriverAdapter driver;
    private final Locator locator;
    private final String semanticType;

    /**
     * @param driver - адаптер драйвера (не null)
     * @param locator - универсальный локатор (не null)
     * @param semanticType - семантический тип (может быть null/пустым)
     */
    public UIElement(DriverAdapter driver, Locator locator, String semanticType) {
        this.driver = Objects.requireNonNull(driver, "driver");
        this.locator = Objects.requireNonNull(locator, "locator");
        this.semanticType = semanticType;
    }

    private ElementHandle handle() { return driver.find(locator); }

    /** Клик по элементу. */
    public void click() { handle().click(); }

    /** Ввод текста (для input-подобных элементов): clear() + sendKeys(). */
    public void type(String text) {
        ElementHandle el = handle();
        el.clear();
        el.sendKeys(text);
    }

    /** Видимый текст элемента. */
    public String text() { return handle().getText(); }

    /** Элемент отображается на экране. */
    public boolean visible() { return handle().isDisplayed(); }

    /** Значение атрибута. */
    public String attr(String name) { return handle().getAttribute(name); }

    /** Семантический тип (как определён в YAML). */
    public String typeName() { return semanticType; }

    @Override public String toString() {
        return "UIElement{" + (semanticType == null ? "generic" : semanticType) + " @ " + locator + "}";
    }
}

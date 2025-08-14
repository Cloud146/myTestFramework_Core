package driver;

/**
 * Абстракция над конкретным элементом UI.
 * Реализация в адаптерах: SeleniumWebElementHandle, AppiumElementHandle, PlaywrightLocatorHandle...
 */
public interface ElementHandle {
    void click();
    void sendKeys(CharSequence... text);
    String getText();
    boolean isDisplayed();
    String getAttribute(String name);

    // При желании: clear(), hover(), submit(), scrollIntoView() и т.д.
    default void clear() {
        // Реализации могут переопределить. По умолчанию no-op.
    }
}

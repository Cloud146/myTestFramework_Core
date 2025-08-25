package driver;

/**
 * Абстракция над конкретным UI-элементом, найденным драйвером.
 * Реализация предоставляется адаптерами (Selenium, Appium, Playwright).
 */
public interface ElementHandle {
    /** Клик по элементу. */
    void click();

    /** Ввод текста (sendKeys). */
    void sendKeys(CharSequence... text);

    /** Получение видимого текста. */
    String getText();

    /** Проверка видимости. */
    boolean isDisplayed();

    /** Значение атрибута. */
    String getAttribute(String name);

    /**
     * Очистка поля ввода. Реализации могут переопределить.
     * По умолчанию — no-op.
     */
    default void clear() { /* no-op */ }
}

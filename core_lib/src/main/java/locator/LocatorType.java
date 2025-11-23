package locator;

/**
 * Поддерживаемые типы локаторов. Часть значений может использоваться адаптерами в будущем.
 */
public enum LocatorType {
    ID,
    XPATH,
    ACCESSIBILITY_ID,
    CLASS_NAME,
    CSS,         // на будущее (веб/Playwright)
    NAME,        // опционально
    TAG_NAME,          // добавить
    LINK_TEXT,         // добавить
    PARTIAL_LINK_TEXT, // добавить
    ANDROID_UIAUTOMATOR, // на будущее для Appium-адаптера
    IOS_PREDICATE,       // на будущее для Appium-адаптера
    IOS_CLASS_CHAIN      // на будущее
}
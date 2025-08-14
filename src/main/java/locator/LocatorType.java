package locator;

public enum LocatorType {
    ID,
    XPATH,
    ACCESSIBILITY_ID,
    CLASS_NAME,
    CSS,         // на будущее (веб/Playwright)
    NAME,        // опционально
    ANDROID_UIAUTOMATOR, // на будущее для Appium-адаптера
    IOS_PREDICATE,       // на будущее для Appium-адаптера
    IOS_CLASS_CHAIN      // на будущее
}
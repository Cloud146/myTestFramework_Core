package capabilities;

/**
 * Контракт для построения capabilities/опций браузера
 * под разные движки (Selenium, Playwright, Appium...).
 *
 * @param <T> тип возвращаемого объекта опций движка
 */
public interface BrowserCapabilities<T> {

    /**
     * Собирает и возвращает объект настроек браузера
     * на основе конфигурации runtime.properties.
     *
     * @return объект с настройками (для Selenium — ChromeOptions/FirefoxOptions/EdgeOptions)
     */
    T buildCapabilities();

    /**
     * Создаёт драйвер на основе настроек.
     */
    Object createDriver(T options);
}

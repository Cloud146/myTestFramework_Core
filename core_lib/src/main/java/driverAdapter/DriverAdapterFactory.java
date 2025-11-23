package driverAdapter;

/**
 * SPI‑контракт для фабрик адаптеров.
 *
 * Каждая реализация (например, SeleniumDriverAdapterFactory) регистрируется
 * через ServiceLoader (META-INF/services/driver.DriverAdapterFactory).
 *
 * Это позволяет рантайму динамически находить доступные движки,
 * не добавляя зависимости в core.
 */
public interface DriverAdapterFactory {

    /**
     * @return имя движка (например: "selenium", "playwright", "appium")
     */
    String getEngineName();

    /**
     * Создать новый {@link DriverAdapter} для выбранного движка.
     *
     * @return инициализированный адаптер
     */
    DriverAdapter create();
}


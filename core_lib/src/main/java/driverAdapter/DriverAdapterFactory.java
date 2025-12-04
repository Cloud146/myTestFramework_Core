package driverAdapter;

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
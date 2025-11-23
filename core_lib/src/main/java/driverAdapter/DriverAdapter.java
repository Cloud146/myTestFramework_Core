package driverAdapter;

import FileUtils.PageReader;
import driverAdapter.adapter_api_contracts.alerts.AlertAdapter;
import driverAdapter.adapter_api_contracts.element.ElementStateAdapter;
import driverAdapter.adapter_api_contracts.element.TextAdapter;
import driverAdapter.adapter_api_contracts.interaction.InputAdapter;
import driverAdapter.adapter_api_contracts.interaction.MouseAdapter;
import driverAdapter.adapter_api_contracts.navigation.NavigationAdapter;
import driverAdapter.adapter_api_contracts.waits.WaitAdapter;

/**
 * Универсальный интерфейс для взаимодействия с элементами UI.
 * Реализуется конкретными адаптерами (Selenium, Playwright, Appium).
 *
 * Тесты и {@link PageReader} работают только с этим интерфейсом,
 * не зная о деталях конкретного движка.
 */
public interface DriverAdapter extends
        AlertAdapter,
        ElementStateAdapter,
        TextAdapter,
        InputAdapter,
        MouseAdapter,
        NavigationAdapter,
        WaitAdapter
{

    /** Закрыть драйвер/браузер/сессию. */
    void close();


}

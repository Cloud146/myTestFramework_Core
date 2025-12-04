package TestUtils.Assertion.executors;

import driverAdapter.AdapterHolder;
import driverAdapter.DriverAdapter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Логика для одиночных жестких проверок (Hard Assertions).
 */
public class SingleAssertionExecutor {

    private DriverAdapter getAdapter() {
        return AdapterHolder.get();
    }

    public void executeTextAssertion(String elementName, String expected, String description) {
        assertThat(getAdapter().getTextFromElement(elementName))
                .as(description)
                .isEqualTo(expected);
    }
}

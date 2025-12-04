package TestUtils.Assertion.executors;

import TestUtils.Assertion.model.AssertionEntry;
import TestUtils.Assertion.model.AssertionFailure;
import TestUtils.Assertion.model.SoftVerificationException;
import TestUtils.Assertion.model.StateAssertionEntry;
import driverAdapter.AdapterHolder;
import driverAdapter.DriverAdapter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Логика для массовых мягких проверок (Soft Assertions).
 */
public class TableAssertionExecutor {

    private DriverAdapter getAdapter() {
        return AdapterHolder.get();
    }

    // --- Массовая проверка текста ---
    public void executeTableTextChecks(List<AssertionEntry> checks) {
        List<AssertionFailure> failures = new ArrayList<>();

        for (AssertionEntry check : checks) {
            String elementName = check.getElement();
            String condition = check.getCondition().trim().toLowerCase();
            String expectedValue = check.getValue() == null ? "" : check.getValue();

            String actualText = "ERROR";

            try {
                actualText = getAdapter().getTextFromElement(elementName);

                String description = String.format("Элемент '%s'", elementName);

                switch (condition) {
                    case "равен": assertThat(actualText).as(description).isEqualTo(expectedValue); break;
                    case "не равен": assertThat(actualText).as(description).isNotEqualTo(expectedValue); break;
                    case "содержит": assertThat(actualText).as(description).contains(expectedValue); break;
                    case "не содержит": assertThat(actualText).as(description).doesNotContain(expectedValue); break;
                    case "равен (без регистра)": assertThat(actualText).as(description).isEqualToIgnoringCase(expectedValue); break;
                    case "пуст": assertThat(actualText).as(description).isNullOrEmpty(); break;
                    case "не пуст": assertThat(actualText).as(description).isNotEmpty(); break;
                    default:
                        throw new RuntimeException("Неизвестное условие: " + condition);
                }

            } catch (AssertionError e) {
                failures.add(new AssertionFailure(elementName, condition, expectedValue, actualText));
            } catch (Exception e) {
                failures.add(new AssertionFailure(elementName, condition, expectedValue, "Exception: " + e.getMessage()));
            }
        }

        if (!failures.isEmpty()) {
            throw new SoftVerificationException(failures);
        }
    }

    // --- Массовая проверка состояний ---
    public void executeStateChecks(List<StateAssertionEntry> checks) {
        List<AssertionFailure> failures = new ArrayList<>();

        for (StateAssertionEntry check : checks) {
            String elementName = check.getElement();
            String state = check.getState().trim().toLowerCase();

            String actualStatus = "unknown";

            try {
                boolean isTrue = false;
                switch (state) {
                    case "отображается":
                    case "виден":
                        isTrue = getAdapter().isElementDisplayed(elementName);
                        actualStatus = isTrue ? "отображается" : "скрыт";
                        assertThat(isTrue).as("Visible").isTrue();
                        break;

                    case "скрыт":
                    case "не отображается":
                        isTrue = getAdapter().isElementDisplayed(elementName);
                        actualStatus = isTrue ? "отображается" : "скрыт";
                        assertThat(isTrue).as("Hidden").isFalse();
                        break;

                    case "активен":
                        isTrue = getAdapter().isElementEnabled(elementName);
                        actualStatus = isTrue ? "активен" : "не активен";
                        assertThat(isTrue).as("Enabled").isTrue();
                        break;

                    case "не активен":
                        isTrue = getAdapter().isElementEnabled(elementName);
                        actualStatus = isTrue ? "активен" : "не активен";
                        assertThat(isTrue).as("Disabled").isFalse();
                        break;

                    case "существует":
                        isTrue = getAdapter().isElementPresent(elementName);
                        actualStatus = isTrue ? "существует" : "не существует";
                        assertThat(isTrue).as("Present").isTrue();
                        break;

                    case "не существует":
                        isTrue = getAdapter().isElementPresent(elementName);
                        actualStatus = isTrue ? "существует" : "не существует";
                        assertThat(isTrue).as("Not Present").isFalse();
                        break;

                    default: throw new RuntimeException("Неизвестное состояние: " + state);
                }

            } catch (AssertionError e) {
                failures.add(new AssertionFailure(elementName, state, state, actualStatus));
            } catch (Exception e) {
                failures.add(new AssertionFailure(elementName, state, state, "Exception: " + e.getMessage()));
            }
        }

        if (!failures.isEmpty()) {
            throw new SoftVerificationException(failures);
        }
    }
}

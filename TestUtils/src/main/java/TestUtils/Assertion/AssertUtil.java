package TestUtils.Assertion;

import TestUtils.Assertion.executors.SingleAssertionExecutor;
import TestUtils.Assertion.executors.TableAssertionExecutor;
import TestUtils.Assertion.helpers.AllureTableUtil;
import TestUtils.Assertion.model.AssertionEntry;
import TestUtils.Assertion.model.AssertionFailure;
import TestUtils.Assertion.model.SoftVerificationException;
import TestUtils.Assertion.model.StateAssertionEntry;
import TestUtils.Assertion.model.FrameworkAssertionError;
import FileUtils.TestDataResolver;
import driverAdapter.adapter_api_contracts.element.TextAdapter;
import io.cucumber.java.DataTableType;
import io.cucumber.java.bg.И;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import logging.Log;
import org.slf4j.Logger;
import java.util.List;
import java.util.Map;

public class AssertUtil {

    private static final Logger log = Log.get(AssertUtil.class);

    private final SingleAssertionExecutor singleExecutor = new SingleAssertionExecutor();
    private final TableAssertionExecutor tableExecutor = new TableAssertionExecutor();

    public AssertUtil(){}

    public AssertUtil(TextAdapter ignored){}

    @И("Проверить текст элемента «(.+)» с ожидаемым текстом «(.+)»$")
    public void assertElementTextEquals(String elementName, String expectedRaw) {
        String expectedResolved  = TestDataResolver.resolve(expectedRaw);
        performTextAssertion(elementName, expectedResolved);
    }

    @Step("Проверка текста элемента: {elementName}")
    private void performTextAssertion(String elementName, String expected) {
        String description = "Проверка текста элемента " + elementName;

        try {
            singleExecutor.executeTextAssertion(elementName, expected, description);
            log.info("Выполнена проверка: {}", description);
        } catch (AssertionError e) {
            log.error("Проверка провалена: {}", description);
            throw new FrameworkAssertionError("Ошибка проверки: " + description + "\n" + e.getMessage(), e);
        }
    }

    @DataTableType
    public AssertionEntry defineAssertionEntry(Map<String, String> entry) {
        return new AssertionEntry(
                entry.get("элемент"),
                entry.get("состояние"),
                entry.get("значение")
        );
    }

    @И("Проверить текст элементов «(.+)»:$")
    public void checkTextConditions(String checkDescription, List<AssertionEntry> checks) {
        checks.forEach(check -> {
            String raw = check.getValue();
            if (raw != null && !raw.isBlank()) {
                check.setValue(TestDataResolver.resolve(raw));
            }
        });

        performTableTextCheck(checkDescription, checks);
    }

    private void performTableTextCheck(String checkDescription, List<AssertionEntry> checks) {
        Allure.step("Проверка текста элементов: " + checkDescription, () -> {
            log.info("Выполняется проверка текста элементов: {}", checkDescription);

            AllureTableUtil.attachTextChecks("Ожидаемый результат", checks);

            try {
                tableExecutor.executeTableTextChecks(checks);
                log.info("Проверка текста элементов завершена успешно: {}", checkDescription);

            } catch (SoftVerificationException e) {
                log.error("Обнаружены ошибки в проверках: {}", checkDescription);
                AllureTableUtil.attachAssertionFailureTable(e.getFailures());

                String detailedMessage = buildFailureMessage(checkDescription, e.getFailures());

                throw new FrameworkAssertionError(detailedMessage);
            }
        });
    }

    @DataTableType
    public StateAssertionEntry defineStateEntry(Map<String, String> entry) {
        return new StateAssertionEntry(
                entry.get("элемент"),
                entry.get("состояние")
        );
    }

    @И("Проверить состояние элементов «(.+)»:$")
    public void checkElementStates(String checkDescription, List<StateAssertionEntry> checks) {
        performStateCheckStep(checkDescription, checks);
    }

    private void performStateCheckStep(String checkDescription, List<StateAssertionEntry> checks) {
        Allure.step("Проверка состояний элементов: " + checkDescription, () -> {
            log.info("Выполняется проверка состояний элементов: {}", checkDescription);

            AllureTableUtil.attachStateChecks("Ожидаемые состояния", checks);

            try {
                tableExecutor.executeStateChecks(checks);
                log.info("Проверка состояний элементов завершена успешно: {}", checkDescription);

            } catch (SoftVerificationException e) {
                log.error("Обнаружены ошибки в состояниях элементов: {}", checkDescription);
                AllureTableUtil.attachAssertionFailureTable(e.getFailures());

                String detailedMessage = buildFailureMessage(checkDescription, e.getFailures());
                throw new FrameworkAssertionError(detailedMessage);
            }
        });
    }

    private String buildFailureMessage(String description, List<AssertionFailure> failures) {
        StringBuilder sb = new StringBuilder();
        sb.append("Провалена проверка: '").append(description).append("'\n");
        sb.append("Найдены несоответствия (").append(failures.size()).append("):\n");
        sb.append("--------------------------------------------------\n");

        int i = 1;
        for (AssertionFailure fail : failures) {
            sb.append(i++).append(") Элемент: '").append(fail.getElement()).append("'\n");
            sb.append("   Условие:    ").append(fail.getCondition()).append("\n");
            sb.append("   Ожидалось:  ").append(fail.getExpected()).append("\n");
            sb.append("   Фактически: ").append(fail.getActual()).append("\n");
            sb.append("--------------------------------------------------\n");
        }
        return sb.toString();
    }
}

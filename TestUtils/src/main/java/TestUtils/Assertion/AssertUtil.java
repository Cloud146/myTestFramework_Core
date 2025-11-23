package TestUtils.Assertion;

import FileUtils.TestDataResolver;
import driverAdapter.AdapterHolder;
import driverAdapter.adapter_api_contracts.element.TextAdapter;
import io.cucumber.java.bg.И;
import io.qameta.allure.Step;
import logging.Log;
import org.slf4j.Logger;
import static org.assertj.core.api.Assertions.assertThat;

public class AssertUtil {

    private static final Logger log = Log.get(AssertUtil.class);

    private static TextAdapter textAdapter;

    public AssertUtil(){
        this.textAdapter = AdapterHolder.get();
    }

    public AssertUtil(TextAdapter adapter){
        textAdapter = adapter;
    }

//    @И("Проверить текст элемента (.+) с ожидаемым текстом (.+)$")
    @И("Проверить текст элемента «(.+)» с ожидаемым текстом «(.+)» c описанием проверки «(.+)»$")
    @Step("Проверка текста элемента: {description}")
    public void assertElementTextEquals(String elementName, String expectedRaw, String description) {
        String expected = TestDataResolver.resolve(expectedRaw);
        assertThat(textAdapter.getTextFromElement(elementName))
                .as(description)
                .isEqualTo(expected);
        log.info("Выполнена проверка: {}", description);
    }
}

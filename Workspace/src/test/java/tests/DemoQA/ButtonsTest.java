package tests.DemoQA;

import TestUtils.Assertion.helpers.CheckBuilder;
import TestUtils.BaseTest.BaseTest;
import TestUtils.JavaParallel.ParallelClass;
import TestUtils.ScreenshotUtil.ScreenshotUtil;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Test Framework")
@Feature("DEMOQA")
@Story("UI")
@ParallelClass(threads = 3)
public class ButtonsTest extends BaseTest {

    @Description("Описание теста")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Тест двойного клика")
    public void testDoubleCLick() {
        navigationSteps.openPage("Buttons Page");

        mouseSteps.doubleClick("Double Click Me");
        assertUtil.assertElementTextEquals("Double Click Message",
                "{DoubleClickMessage}");

        assertUtil.checkTextConditions("Проверка текста сообщения после клика по кнопке 'Double Click Me'",
                CheckBuilder.text()
                        .add("Double Click Message", "равен", "{DoubleClickMessage}")
                        .add("Double Click Me", "равен", "Double Click Me")
                        .build()
        );

        assertUtil.checkElementStates("Видимость кнопок",
                CheckBuilder.state()
                        .add("Double Click Me", "активен")
                        .add("Right Click Me", "активен")
                        .add("Click Me", "активен")
                        .build()
        );

        ScreenshotUtil.takeScreenshot("Скрин страницы \"Buttons Page\"");
    }

    @Test(description = "Тест правого клика")
    public void testRightClick(){
        navigationSteps.openPage("Buttons Page");

        mouseSteps.rightClick("Right Click Me");
        assertUtil.assertElementTextEquals("Right Click Message",
                "{RightClickMessage}");
    }

    @Test(description = "Тест клика")
    public void testDynamicClick(){
        navigationSteps.openPage("Buttons Page");

        mouseSteps.click("Click Me");
        assertUtil.assertElementTextEquals("Dynamic Click Message",
                "{DynamicClickMessage}");
    }
}

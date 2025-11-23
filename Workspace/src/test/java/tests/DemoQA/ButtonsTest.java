package tests.DemoQA;

import TestUtils.BaseTest.BaseTest;
import TestUtils.ScreenshotUtil.ScreenshotUtil;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.annotations.Test;

@Epic("Test Framework")
@Feature("DEMOQA")
@Story("UI")
public class ButtonsTest extends BaseTest {

    @Description("Описание теста")
    @Severity(SeverityLevel.NORMAL)
    @Tag("smoke")
    @Test(description = "Тест двойного клика")
    public void testDoubleCLick() {
        navigationSteps.openPage("Buttons Page");

        mouseSteps.doubleClick("Double Click Me");
        assertUtil.assertElementTextEquals("Double Click Message",
                "{DoubleClickMessage.значение}",
                "Проверка текста сообщения после клика по кнопке 'Double Click Me'");

        ScreenshotUtil.takeScreenshot("Скрин страницы \"Buttons Page\"");
    }

    @Test(description = "Тест правого клика")
    public void testRightClick(){
        navigationSteps.openPage("Buttons Page");

        mouseSteps.rightClick("Right Click Me");
        assertUtil.assertElementTextEquals("Right Click Message",
                "{RightClickMessage.значение}",
                "Проверка текста сообщения после клика по кнопке 'Right Click Me'");
    }

    @Test(priority = 3, description = "Тест клика")
    public void testDynamicClick(){
        navigationSteps.openPage("Buttons Page");

        mouseSteps.click("Click Me");
        assertUtil.assertElementTextEquals("Dynamic Click Message",
                "{DynamicClickMessage.значение}",
                "Проверка текста сообщения после клика по кнопке 'Click Me'");
    }
}

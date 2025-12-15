package TestUtils.ScreenshotUtil;

import io.cucumber.java.ru.И;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Step;
import logging.Log;
import org.slf4j.Logger;
import org.testng.ITestResult;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class ScreenshotUtil {

    private static final Logger log = Log.get(ScreenshotUtil.class);

    private ScreenshotUtil(){}

    public static byte[] captureScreenPngBytes() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screen = robot.createScreenCapture(screenRect);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screen, "png", baos);
            return baos.toByteArray();
        } catch (AWTException | java.io.IOException e) {
            throw new RuntimeException("Не удалось снять скриншот", e);
        }
    }

    @И("Сделать скриншот экрана и назвать файл (.+)$")
    @Step("Скриншот экрана. Название файла скриншота: {screenshotName}")
    public static void takeScreenshot(String screenshotName) {
        try {
            byte[] png = captureScreenPngBytes();
            attachToAllure(screenshotName, png);
        } catch (Throwable t) {
            log.info("Не удалось сделать скриншот: {}", t.getMessage());
        }
    }

    @Step("Скриншот экрана при возникновении ошибки")
    public static boolean takeScreenshotOnFailure(ITestResult result) {
        if (result == null) return false;
        String name = "Screenshot on failure";
        try {
            byte[] png = captureScreenPngBytes();
            return attachToAllure(name, png);
        } catch (Throwable t) {
            log.info("Ошибка при создании скриншота для {} : {}", name, t.getMessage());
            return false;
        }
    }

    private static boolean attachToAllure(String name, byte[] png) {
        try {
            AllureLifecycle lifecycle = Allure.getLifecycle();
            if (lifecycle == null) {
                log.debug("AllureLifecycle == null");
                return false;
            }
            lifecycle.addAttachment(name, "image/png", "png", png);
            return true;
        } catch (RuntimeException re) {
            log.debug("Could not add attachment: {}", re.getMessage());
            return false;
        }
    }

    private static String safeMethodName(ITestResult result) {
        try {
            return result.getMethod() == null ? "unknown" : result.getMethod().getMethodName();
        } catch (Throwable t) {
            return "unknown";
        }
    }
}

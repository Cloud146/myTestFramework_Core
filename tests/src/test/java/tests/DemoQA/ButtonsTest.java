package tests.DemoQA;

import data.TestDataReader;
import driver.DriverManager;
import driver.SeleniumDriverAdapter;
import element.ElementFinder;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ButtonsTest {

    private WebDriver driver;
    private ElementFinder finder;
    private TestDataReader data;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();

        // Новый ElementFinder сам найдёт ButtonsPage.yaml рядом с этим тестом
        finder = new ElementFinder(new SeleniumDriverAdapter(driver), this.getClass());

        data = new TestDataReader(this.getClass());

        // URL подставляем из meta.url в YAML
        if (finder.getPageUrl() == null || finder.getPageUrl().isBlank()) {
            throw new IllegalStateException("В YAML не указан meta.url для страницы");
        }
        driver.get(finder.getPageUrl());
    }

    @Test
    public void testClickButton() {
        finder.get("buttonClickMe").click();

        String text = finder.get("messageText").text();

        String expected = data.get("expected.clickMessage");
        Assert.assertEquals(text.trim(), expected);
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}

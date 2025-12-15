package runner;

import TestUtils.CucumberUtils.BaseCucumberRunner;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        // Указываем путь к фичам в ЭТОМ (пользовательском) репозитории
        features = "classpath:features",

        // (Опционально) Если пользователь написал свои кастомные Java-шаги,
        // он может добавить их пакет сюда. Но базовые шаги ("steps") уже подключены в родителе.
        glue = {
                "steps",                   // Пакет steps из core_lib
                "TestUtils.CucumberUtils", // Хуки
                "TestUtils.Assertion"      // Ассерты
        },
        monochrome = true
)
public class CucumberRunTests extends BaseCucumberRunner {
}

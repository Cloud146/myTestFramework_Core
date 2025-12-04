package TestUtils.Assertion.model;

/**
 * Специальная ошибка для упавших проверок (Assertions).
 * Наследует AssertionError, чтобы в отчетах тест был помечен как Failed (Красный), а не Broken.
 */
public class FrameworkAssertionError extends AssertionError {

    public FrameworkAssertionError(String message) {
        super(message);
    }

    public FrameworkAssertionError(String message, Throwable cause) {
        super(message, cause);
    }
}

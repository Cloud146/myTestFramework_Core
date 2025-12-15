package TestUtils.Assertion.model;

public class FrameworkAssertionError extends AssertionError {

    public FrameworkAssertionError(String message) {
        super(message);
    }

    public FrameworkAssertionError(String message, Throwable cause) {
        super(message, cause);
    }
}

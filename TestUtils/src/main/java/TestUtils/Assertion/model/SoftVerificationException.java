package TestUtils.Assertion.model;

import java.util.List;

public class SoftVerificationException extends RuntimeException {
    private final List<AssertionFailure> failures;

    public SoftVerificationException(List<AssertionFailure> failures) {
        super("Обнаружено " + failures.size() + " ошибок в проверках");
        this.failures = failures;
    }

    public List<AssertionFailure> getFailures() {
        return failures;
    }
}
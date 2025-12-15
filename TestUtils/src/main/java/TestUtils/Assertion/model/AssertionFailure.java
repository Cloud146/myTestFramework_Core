package TestUtils.Assertion.model;

public class AssertionFailure {
    private final String element;
    private final String condition;
    private final String expected;
    private final String actual;

    public AssertionFailure(String element, String condition, String expected, String actual) {
        this.element = element;
        this.condition = condition;
        this.expected = expected;
        this.actual = actual;
    }

    public String getElement() { return element; }
    public String getCondition() { return condition; }
    public String getExpected() { return expected; }
    public String getActual() { return actual; }
}

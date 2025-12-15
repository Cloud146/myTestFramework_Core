package TestUtils.Assertion.model;

public class AssertionEntry {
    private String element;
    private String condition;
    private String value;

    public AssertionEntry() {}

    public AssertionEntry(String element, String condition, String value) {
        this.element = element;
        this.condition = condition;
        this.value = value;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("{Элемент: '%s', Условие: '%s', Ожидается: '%s'}", element, condition, value);
    }
}

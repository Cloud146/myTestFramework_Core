package TestUtils.Assertion.model;

public class StateAssertionEntry {
    private String element;
    private String state;

    public StateAssertionEntry() {}

    public StateAssertionEntry(String element, String state) {
        this.element = element;
        this.state = state;
    }

    public String getElement() { return element; }
    public void setElement(String element) { this.element = element; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    @Override
    public String toString() {
        return String.format("{Элемент: '%s', Состояние: '%s'}", element, state);
    }
}
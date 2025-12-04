package TestUtils.Assertion.helpers;

import TestUtils.Assertion.model.AssertionEntry;
import TestUtils.Assertion.model.StateAssertionEntry;

import java.util.ArrayList;
import java.util.List;

public class CheckBuilder {

    private CheckBuilder() {}

    // --- Строитель для текстовых проверок ---
    public static TextChecks text() {
        return new TextChecks();
    }

    // --- Строитель для проверок состояний ---
    public static StateChecks state() {
        return new StateChecks();
    }

    // Внутренний класс для текста
    public static class TextChecks {
        private final List<AssertionEntry> checks = new ArrayList<>();

        public TextChecks add(String element, String condition, String value) {
            checks.add(new AssertionEntry(element, condition, value));
            return this;
        }

        public List<AssertionEntry> build() {
            return checks;
        }
    }

    // Внутренний класс для состояний
    public static class StateChecks {
        private final List<StateAssertionEntry> checks = new ArrayList<>();

        public StateChecks add(String element, String state) {
            checks.add(new StateAssertionEntry(element, state));
            return this;
        }

        public List<StateAssertionEntry> build() {
            return checks;
        }
    }
}
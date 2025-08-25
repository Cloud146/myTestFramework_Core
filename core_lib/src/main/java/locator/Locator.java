package locator;

import java.util.Objects;

/**
 * Универсальный локатор: тип + значение. Не зависит от конкретного драйвера.
 */
public final class Locator {
    private final LocatorType type;
    private final String value;

    /**
     * @param type тип локатора (не null)
     * @param value значение локатора (не null)
     */
    public Locator(LocatorType type, String value) {
        this.type = Objects.requireNonNull(type, "type");
        this.value = Objects.requireNonNull(value, "value");
    }

    /** @return тип локатора */
    public LocatorType getType() { return type; }

    /** @return значение локатора */
    public String getValue() { return value; }

    @Override public String toString() { return type + ":" + value; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locator)) return false;
        Locator locator = (Locator) o;
        return type == locator.type && value.equals(locator.value);
    }

    @Override public int hashCode() {
        return Objects.hash(type, value);
    }
}
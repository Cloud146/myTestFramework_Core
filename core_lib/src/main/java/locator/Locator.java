package locator;

import java.util.Objects;

public final class Locator {
    private final LocatorType type;
    private final String value;

    public Locator(LocatorType type, String value) {
        this.type = Objects.requireNonNull(type, "type");
        this.value = Objects.requireNonNull(value, "value");
    }

    public LocatorType getType() { return type; }

    public String getValue() { return value; }

    @Override
    public String toString() { return type + ":" + value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locator)) return false;
        Locator locator = (Locator) o;
        return type == locator.type && value.equals(locator.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
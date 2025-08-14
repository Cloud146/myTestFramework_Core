package data;

import locator.LocatorType;

public class ElementDefinition {
    private String type;            // семантический тип элемента: button, input, checkbox...
    private String locatorType;     // строковое имя enum LocatorType
    private String locatorValue;

    public String getType() { return type; }
    public String getLocatorType() { return locatorType; }
    public String getLocatorValue() { return locatorValue; }

    // Удобные преобразователи:
    public LocatorType toLocatorTypeEnum() {
        return LocatorType.valueOf(locatorType.toUpperCase().replace('-', '_'));
    }
}
package data;

import ErrorUtils.ErrorBuilder;
import locator.LocatorType;

public class ElementDefinition {

    private String type;

    private String locatorType;

    private String locatorValue;

    public String getType() { return type; }

    public String getLocatorType() { return locatorType; }

    public String getLocatorValue() { return locatorValue; }

    public void setType(String type) { this.type = type; }

    public void setLocatorType(String locatorType) { this.locatorType = locatorType; }

    public void setLocatorValue(String locatorValue) { this.locatorValue = locatorValue; }

    public LocatorType toLocatorTypeEnum() {
        if (locatorType == null || locatorType.isBlank()) {
            ErrorBuilder.forAction("parse locatorType")
                    .selector(locatorType)
                    .message("Некорректный или пустой тип локатора")
                    .raise();
        }
        String normalized = locatorType.trim().toUpperCase().replace('-', '_');
        try {
            return LocatorType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            ErrorBuilder.forAction("parse locatorType")
                    .selector(locatorType)
                    .causedBy(ex)
                    .message("Неизвестный тип локатора: " + locatorType)
                    .raise();
            throw ex;
        }
    }
}
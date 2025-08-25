package data;

import exceptions.InvalidLocatorException;
import locator.LocatorType;

/**
 * Описание элемента страницы из YAML.
 * Поддерживает две схемы YAML:
 * 1) Плоскую: locatorType + locatorValue
 * 2) Вложенную: locator { by, value } — на случай адаптеров с разными типами локаторов
 */
public class ElementDefinition {
    /** Семантический тип элемента: button, input, checkbox... */
    private String type;

    /** Строковое имя enum LocatorType (для плоской схемы) */
    private String locatorType;

    /** Значение локатора (для плоской схемы) */
    private String locatorValue;

    // Геттеры для SnakeYAML/потребителей
    public String getType() { return type; }
    public String getLocatorType() { return locatorType; }
    public String getLocatorValue() { return locatorValue; }

    // Сеттеры важны для корректной десериализации SnakeYAML (JavaBean-конвенция)
    public void setType(String type) { this.type = type; }
    public void setLocatorType(String locatorType) { this.locatorType = locatorType; }
    public void setLocatorValue(String locatorValue) { this.locatorValue = locatorValue; }

    /**
     * Конвертация строкового типа локатора в enum.
     * Допускает тире в YAML (преобразуются в подчёркивания), регистр игнорируется.
     *
     * @return LocatorType
     * @throws InvalidLocatorException если значение отсутствует или не распознано
     */
    public LocatorType toLocatorTypeEnum() {
        if (locatorType == null || locatorType.isBlank()) {
            throw new InvalidLocatorException("locatorType is null/blank");
        }
        String normalized = locatorType.trim().toUpperCase().replace('-', '_');
        try {
            return LocatorType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new InvalidLocatorException("Unknown locatorType: " + locatorType);
        }
    }
}
package data;

import ErrorUtils.ErrorBuilder;
import locator.LocatorType;

/**
 * Описание элемента страницы из YAML.
 * Поддерживает две схемы YAML:
 * 1) Плоскую: locatorType + locatorValue
 * 2) Вложенную: locator { by, value } — маппится лоадером в плоские поля
 */
public class ElementDefinition {
    /** Семантический тип элемента: button, input, checkbox... */
    private String type;

    /** Строковое имя enum LocatorType (для плоской схемы) */
    private String locatorType;

    /** Значение локатора (для плоской схемы) */
    private String locatorValue;

    /** @return семантический тип */
    public String getType() { return type; }

    /** @return строковое имя типа локатора */
    public String getLocatorType() { return locatorType; }

    /** @return значение локатора */
    public String getLocatorValue() { return locatorValue; }

    /** @param type семантический тип */
    public void setType(String type) { this.type = type; }

    /** @param locatorType строковое имя типа локатора */
    public void setLocatorType(String locatorType) { this.locatorType = locatorType; }

    /** @param locatorValue значение локатора */
    public void setLocatorValue(String locatorValue) { this.locatorValue = locatorValue; }

    /**
     * Конвертация строкового типа локатора в enum.
     * Допускает тире в YAML (преобразуются в подчёркивания), регистр игнорируется.
     *
     * @return LocatorType
     */
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
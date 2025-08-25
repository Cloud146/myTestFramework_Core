package data;

import java.util.Map;

/**
 * Модель страницы: имя элемента -> его описание.
 * Обрати внимание: сам YamlPageLoader умеет принимать YAML как в виде
 *   elements: { ... }
 * так и в виде корневого словаря { elementName: { ... } } и преобразует к PageModel.
 */
public class PageModel {
    private PageMeta meta;
    private Map<String, ElementDefinition> elements;

    public PageMeta getMeta() { return meta; }
    public void setMeta(PageMeta meta) { this.meta = meta; }

    /** @return словарь элементов страницы */
    public Map<String, ElementDefinition> getElements() { return elements; }

    /** @param elements словарь элементов страницы */
    public void setElements(Map<String, ElementDefinition> elements) { this.elements = elements; }
}

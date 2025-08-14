package data;

import java.util.Map;

/** Модель страницы: имя элемента -> его описание. */
public class PageModel {
    private Map<String, ElementDefinition> elements;

    public Map<String, ElementDefinition> getElements() { return elements; }
    public void setElements(Map<String, ElementDefinition> elements) { this.elements = elements; }
}

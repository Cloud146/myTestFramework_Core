package data;

import java.util.Map;

/**
 * Модель страницы, загружаемой из YAML.
 *
 * Содержит:
 * - PageInfo: метаданные страницы (project, feature, name, url)
 * - elements: словарь элементов страницы (имя элемента -> ElementDefinition)
 *
 * YamlPageLoader поддерживает оба формата YAML:
 * - elements: { ... }
 * - корневой словарь { PageInfo: { ... }, elementName: { ... } }
 */
public class PageModel {

    /** Метаданные страницы (project, feature, name, url). */
    private PageInfo pageInfo;

    /** Словарь элементов страницы: имя элемента -> его описание. */
    private Map<String, ElementDefinition> elements;

    /** Вернуть метаданные страницы. */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /** Установить метаданные страницы. */
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    /** Вернуть словарь элементов страницы. */
    public Map<String, ElementDefinition> getElements() {
        return elements;
    }

    /** Установить словарь элементов страницы. */
    public void setElements(Map<String, ElementDefinition> elements) {
        this.elements = elements;
    }
}

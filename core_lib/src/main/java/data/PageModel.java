package data;

import java.util.Map;

public class PageModel {

    private PageInfo pageInfo;

    private Map<String, ElementDefinition> elements;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Map<String, ElementDefinition> getElements() {
        return elements;
    }

    public void setElements(Map<String, ElementDefinition> elements) {
        this.elements = elements;
    }
}

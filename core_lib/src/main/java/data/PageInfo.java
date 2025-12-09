package data;

/**
 * Метаданные страницы из YAML.
 *
 * Содержит:
 * - project: имя проекта/домена (например, "DemoQA")
 * - feature: функциональный модуль (например, "Buttons")
 * - name: человекочитаемое имя страницы (например, "Buttons Page")
 * - url: базовый URL страницы (например, "https://demoqa.com/buttons")
 */
public class PageInfo {

    private String project;
    private String feature;
    private String name;
    private String url;

    /** Вернуть project. */
    public String getProject() {
        return project;
    }

    /** Установить project. */
    public void setProject(String project) {
        this.project = project;
    }

    /** Вернуть feature. */
    public String getFeature() {
        return feature;
    }

    /** Установить feature. */
    public void setFeature(String feature) {
        this.feature = feature;
    }

    /** Вернуть name. */
    public String getName() {
        return name;
    }

    /** Установить name. */
    public void setName(String name) {
        this.name = name;
    }

    /** Вернуть url. */
    public String getUrl() {
        return url;
    }

    /** Установить url. */
    public void setUrl(String url) {
        this.url = url;
    }
}
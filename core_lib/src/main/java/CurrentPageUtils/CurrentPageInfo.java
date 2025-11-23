package CurrentPageUtils;

public class CurrentPageInfo {

    private CurrentPageInfo() {}

    /** Вернуть project текущей страницы */
    public static String getProject() {
        return PageContext.getHolder().get().getProject();
    }

    /** Вернуть feature текущей страницы */
    public static String getFeature() {
        return PageContext.getHolder().get().getFeature();
    }

    /** Вернуть name текущей страницы */
    public static String getName() {
        return PageContext.getHolder().get().getPageName();
    }

    /** Вернуть url текущей страницы */
    public static String getUrl() {
        return PageContext.getHolder().get().getPageUrl();
    }
}

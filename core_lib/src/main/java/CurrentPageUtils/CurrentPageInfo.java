package CurrentPageUtils;

public class CurrentPageInfo {

    private CurrentPageInfo() {}

    public static String getProject() {
        return PageContext.getHolder().get().getProject();
    }

    public static String getFeature() {
        return PageContext.getHolder().get().getFeature();
    }

    public static String getName() {
        return PageContext.getHolder().get().getPageName();
    }

    public static String getUrl() {
        return PageContext.getHolder().get().getPageUrl();
    }
}

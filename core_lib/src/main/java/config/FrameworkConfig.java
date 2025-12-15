package config;

public final class FrameworkConfig {
    private FrameworkConfig() {}

    public static String pagesBasePath() {
        return System.getProperty("pages.basePath", "pages");
    }
}
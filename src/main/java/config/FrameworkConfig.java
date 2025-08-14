package config;

public final class FrameworkConfig {
    private FrameworkConfig() {}

    /** Базовая папка с описаниями страниц (в resources). Можно переопределить -Dpages.basePath=... */
    public static String pagesBasePath() {
        return System.getProperty("pages.basePath", "pages");
    }
}
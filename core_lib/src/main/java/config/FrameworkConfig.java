package config;

/**
 * Глобальные конфигурационные параметры фреймворка.
 */
public final class FrameworkConfig {
    private FrameworkConfig() {}

    /**
     * Базовая папка с YAML-файлами страниц (в resources).
     * Может быть переопределена системным свойством -Dpages.basePath=...
     *
     * @return базовый путь к папкам с описанием страниц (относительно classpath)
     */
    public static String pagesBasePath() {
        return System.getProperty("pages.basePath", "pages");
    }
}
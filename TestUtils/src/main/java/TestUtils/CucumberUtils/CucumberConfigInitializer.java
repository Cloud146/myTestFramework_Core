package TestUtils.CucumberUtils;

public final class CucumberConfigInitializer {

    private CucumberConfigInitializer() {}

    public static CucumberRuntimeConfig initAndFilter() {
        CucumberRuntimeConfig config = CucumberRuntimeConfig.load();
        config.applySystemFilter();
        return config;
    }
}


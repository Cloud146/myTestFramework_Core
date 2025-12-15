package browsers;

import java.util.ArrayList;
import java.util.List;

public class BrowserConfig {

    private String browser;
    private String driverVersion;
    private String driverPath;
    private String binaryPath;
    private boolean headless;
    private WindowConfig window;
    private List<String> args;
    private String pageLoadStrategy;
    private List<String> preferences;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    public String getBinaryPath() { return binaryPath; }
    public void setBinaryPath(String binaryPath) { this.binaryPath = binaryPath; }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public WindowConfig getWindow() {
        return window != null ? window : new WindowConfig();
    }

    public void setWindow(WindowConfig window) {
        this.window = window;
    }

    public List<String> getArgs() {
        return args != null ? args : new ArrayList<>();
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getPageLoadStrategy() {
        return pageLoadStrategy != null ? pageLoadStrategy : "normal";
    }

    public void setPageLoadStrategy(String pageLoadStrategy) {
        this.pageLoadStrategy = pageLoadStrategy;
    }

    public List<String> getPreferences() {
        return preferences != null ? preferences : new ArrayList<>();
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public static class WindowConfig {

        private String mode;

        public String getMode() {
            return mode != null ? mode : "";
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}

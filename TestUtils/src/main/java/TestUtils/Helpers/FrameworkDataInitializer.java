package TestUtils.Helpers;

import FileUtils.TestDataService;
import config.RuntimeReader;
import java.util.Map;

public final class FrameworkDataInitializer {

    private FrameworkDataInitializer() {}

    public static Map<String, Object> initTimeouts() {
        return RuntimeReader.getSection("timeouts");
    }

    public static int initRetryCount() {
        Map<String, Object> retry = RuntimeReader.getSection("retry");
        return Integer.parseInt(retry.getOrDefault("count", 1).toString());
    }

    public static void initTestData() {
        TestDataService.initIfNeeded();
    }
}


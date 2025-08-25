package data;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TestDataReader {

    private final Map<String, Object> data;

    public TestDataReader(Class<?> testClass) {
        try {
            Path testDir = Paths.get("src", "test", "java")
                    .resolve(testClass.getPackageName().replace('.', File.separatorChar));
            String baseName = testClass.getSimpleName().replace("Test", "Data.yaml");
            File file = testDir.resolve(baseName).toFile();

            if (!file.exists()) {
                throw new IllegalStateException("Test data file not found: " + file.getPath());
            }

            try (InputStream is = new FileInputStream(file)) {
                Yaml yaml = new Yaml();
                Object root = yaml.load(is);
                if (!(root instanceof Map)) {
                    throw new IllegalStateException("Test data YAML has invalid format");
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> rootMap = (Map<String, Object>) root;

                if (!rootMap.containsKey("TestData")) {
                    throw new IllegalStateException("Key 'TestData' not found in YAML: " + file.getPath());
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> td = (Map<String, Object>) rootMap.get("TestData");
                this.data = td;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        String[] keys = path.split("\\.");
        Object current = data;
        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
            } else {
                throw new IllegalArgumentException("Invalid path in test data: " + path);
            }
        }
        return (T) current;
    }
}

package FileUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TestDataResolver {

    private static final Pattern REF_PATTERN = Pattern.compile("\\{([^}]+)\\}");

    private TestDataResolver() {}

    public static String resolve(String raw) {
        if (raw == null) return null;

        Matcher m = REF_PATTERN.matcher(raw.trim());
        if (m.matches()) {
            String key = m.group(1);

            Object val = TestDataService.get(key);

            if (val == null) {
                throw new IllegalStateException("Тестовые данные не найдены по ключу: '" + key + "'");
            }
            return String.valueOf(val);
        }

        return raw;
    }
}
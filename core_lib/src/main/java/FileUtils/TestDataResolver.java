package FileUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TestDataResolver {

    private static final Pattern REF_PATTERN = Pattern.compile("\\{([^}]+)\\}");

    private TestDataResolver() {}

    public static String resolve(String raw) {
        if (raw == null) return null;

        Matcher m = REF_PATTERN.matcher(raw.trim());
        if (m.matches()) {
            String expr = m.group(1); // например "DoubleClickMessage.значение"
            String[] parts = expr.split("\\.");
            String key = parts[0];
            String field = parts.length > 1 ? parts[1] : "значение";

            Map<String, Object> item = TestDataService.item(key);
            Object val = item.get(field);
            if (val == null) {
                throw new IllegalStateException("Нет поля '" + field + "' для ключа '" + key + "'");
            }
            return String.valueOf(val);
        }

        // иначе — это просто текст
        return raw;
    }
}
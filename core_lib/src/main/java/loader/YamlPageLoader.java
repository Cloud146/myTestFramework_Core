package loader;

import config.FrameworkConfig;
import data.ElementDefinition;
import data.PageModel;
import exceptions.PageFileNotFoundException;
import data.PageMeta;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Загрузчик YAML-страниц.
 * Поддерживает:
 * - расширения .yaml и .yml (первым делом ищется .yaml, затем .yml)
 * - две структуры YAML:
 *     A) elements: { elementName: { ... } }
 *     B) { elementName: { ... } }  (корневые ключи — имена элементов)
 * - вложенный блок locator: { by, value } и плоскую форму locatorType/locatorValue
 */
public final class YamlPageLoader {

    /**
     * Загружает модель страницы по относительному пути (без расширения).
     * Например, "android/LoginPage" → pages/android/LoginPage.yaml (или .yml).
     *
     * @param pagePath путь без расширения относительно FrameworkConfig.pagesBasePath()
     * @return PageModel
     * @throws PageFileNotFoundException если файл не найден
     * @throws IllegalStateException     если YAML пустой/битый
     */
    public PageModel load(String pagePath) {
        String base = FrameworkConfig.pagesBasePath();

        // Сначала пробуем .yaml, затем .yml
        String resourceYaml = base + "/" + pagePath + ".yaml";
        String resourceYml  = base + "/" + pagePath + ".yml";

        InputStream is = getResource(resourceYaml);
        if (is == null) {
            is = getResource(resourceYml);
            if (is == null) {
                throw new PageFileNotFoundException("YAML page file not found: " + resourceYaml + " or " + resourceYml);
            }
        }

        Yaml yaml = new Yaml();
        Object root = yaml.load(is);
        if (root == null) {
            throw new IllegalStateException("YAML is empty: " + (is == null ? resourceYaml : resourceYaml + " | " + resourceYml));
        }

        // Унифицируем структуру в Map<String, Object>
        Map<?, ?> map = asMap(root);
        PageModel model = new PageModel();

        // PageInfo
        if (map.containsKey("PageInfo")) {
            Map<?, ?> infoMap = asMap(map.get("PageInfo"));
            PageMeta meta = new PageMeta();
            if (infoMap.get("name") != null) meta.setName(String.valueOf(infoMap.get("name")));
            if (infoMap.get("url") != null) meta.setUrl(String.valueOf(infoMap.get("url")));
            model.setMeta(meta);
        }

        // --- elements ---
        Map<String, ElementDefinition> elements;
        if (map.containsKey("elements")) {
            elements = toElementDefinitions(asMap(map.get("elements")));
        } else {
            elements = toElementDefinitions(map);
        }
        model.setElements(elements);

        return model;
    }

    // --- Перегрузка для загрузки из конкретного файла ---
    public PageModel load(File file) {
        try (InputStream is = new java.io.FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Object root = yaml.load(is);
            if (root == null) {
                throw new IllegalStateException("YAML is empty: " + file.getPath());
            }

            Map<?, ?> map = asMap(root);
            PageModel model = new PageModel();

            // PageInfo
            if (map.containsKey("PageInfo")) {
                Map<?, ?> infoMap = asMap(map.get("PageInfo"));
                PageMeta meta = new PageMeta();
                if (infoMap.get("name") != null) meta.setName(String.valueOf(infoMap.get("name")));
                if (infoMap.get("url") != null) meta.setUrl(String.valueOf(infoMap.get("url")));
                model.setMeta(meta);
            }

            // elements
            Map<String, ElementDefinition> elements;
            if (map.containsKey("elements")) {
                elements = toElementDefinitions(asMap(map.get("elements")));
            } else {
                elements = toElementDefinitions(map);
            }
            model.setElements(elements);

            return model;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML page file: " + file.getPath(), e);
        }
    }


    private InputStream getResource(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    @SuppressWarnings("unchecked")
    private Map<String, ElementDefinition> toElementDefinitions(Map<?, ?> raw) {
        Map<String, ElementDefinition> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> e : raw.entrySet()) {
            String name = String.valueOf(e.getKey());
            Map<?, ?> def = asMap(e.getValue());

            ElementDefinition ed = new ElementDefinition();

            // Семантический тип (не обязателен)
            Object type = def.get("type");
            if (type != null) ed.setType(String.valueOf(type));

            // Поддержка двух форм локатора:
            // 1) locatorType + locatorValue
            Object lt = def.get("locatorType");
            Object lv = def.get("locatorValue");

            // 2) locator { by, value }
            Object locatorObj = def.get("locator");
            if ((lt == null || lv == null) && locatorObj instanceof Map) {
                Map<?, ?> locMap = asMap(locatorObj);
                lt = (lt == null) ? locMap.get("by") : lt;
                lv = (lv == null) ? locMap.get("value") : lv;
            }

            if (lt != null) ed.setLocatorType(String.valueOf(lt));
            if (lv != null) ed.setLocatorValue(String.valueOf(lv));

            result.put(name, ed);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<?, ?> asMap(Object obj) {
        if (obj instanceof Map) return (Map<?, ?>) obj;
        throw new IllegalStateException("Invalid YAML structure: expected mapping, got " + obj);
    }
}

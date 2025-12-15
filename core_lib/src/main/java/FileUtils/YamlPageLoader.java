package FileUtils;

import ErrorUtils.ErrorBuilder;
import config.FrameworkConfig;
import data.ElementDefinition;
import data.PageInfo;
import data.PageModel;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.*;
import java.util.stream.Collectors;

public final class YamlPageLoader {

    public PageModel load(String pagePath) {
        String base = FrameworkConfig.pagesBasePath();

        String resourceYaml = base + "/" + pagePath + ".yaml";
        String resourceYml = base + "/" + pagePath + ".yml";

        InputStream is = getResource(resourceYaml);
        if (is == null) {
            is = getResource(resourceYml);
            if (is == null) {
                ErrorBuilder.forAction("load page model")
                        .selector(pagePath)
                        .message("YAML page file not found: " + resourceYaml + " or " + resourceYml)
                        .raise();
            }
        }
        return parseYaml(is, pagePath);
    }

    public PageModel load(File file) {
        try (InputStream is = new FileInputStream(file)) {
            return parseYaml(is, file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load YAML page file: " + file.getPath(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private PageModel parseYaml(InputStream is, String sourceId) {
        Yaml yaml = new Yaml();
        Object root = yaml.load(is);

        if (root == null) {
            throw new IllegalStateException("YAML is empty: " + sourceId);
        }

        Map<?, ?> map = asMap(root);
        PageModel model = new PageModel();

        if (map.containsKey("PageInfo")) {
            Map<?, ?> infoMap = asMap(map.get("PageInfo"));
            PageInfo info = new PageInfo();
            if (infoMap.get("project") != null) info.setProject(String.valueOf(infoMap.get("project")));
            if (infoMap.get("feature") != null) info.setFeature(String.valueOf(infoMap.get("feature")));
            if (infoMap.get("name") != null) info.setName(String.valueOf(infoMap.get("name")));
            if (infoMap.get("url") != null) info.setUrl(String.valueOf(infoMap.get("url")));
            model.setPageInfo(info);
        }

        Map<String, ElementDefinition> elements;
        if (map.containsKey("elements")) {
            elements = toElementDefinitions(asMap(map.get("elements")));
        } else {
            Map<String, Object> elementsOnly = map.entrySet().stream()
                    .filter(e -> !"PageInfo".equalsIgnoreCase(String.valueOf(e.getKey())))
                    .collect(Collectors.toMap(
                            e -> String.valueOf(e.getKey()),
                            Map.Entry::getValue
                    ));
            elements = toElementDefinitions(elementsOnly);
        }
        model.setElements(elements);

        return model;
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

            Object type = def.get("type");
            if (type != null) ed.setType(String.valueOf(type));

            Object lt = def.get("locatorType");
            Object lv = def.get("locatorValue");

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
        if (obj instanceof Map) {
            return (Map<?, ?>) obj;
        }
        throw new IllegalStateException("Invalid YAML structure: expected mapping, got " + obj);
    }
}
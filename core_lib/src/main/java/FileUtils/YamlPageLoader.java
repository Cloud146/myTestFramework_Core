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

/**
 * Загрузчик YAML-страниц.
 * Поддерживает загрузку:
 *   по относительному пути из classpath (с расширением .yaml или .yml)
 *   из конкретного файла на диске
 * Преобразует YAML в {@link PageModel}, включая:
 *   PageInfo (project, feature, name, url)
 *   elements (описания элементов с локаторами)
 */
public final class YamlPageLoader {

    /**
     * Загружает модель страницы по относительному пути (без расширения).
     * Например: "android/LoginPage" → pages/android/LoginPage.yaml (или .yml).
     *
     * @param pagePath путь без расширения относительно {@link FrameworkConfig#pagesBasePath()}
     * @return заполненная {@link PageModel}
     */
    public PageModel load(String pagePath) {
        String base = FrameworkConfig.pagesBasePath();

        // Сначала пробуем .yaml, затем .yml
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

    /**
     * Загружает модель страницы из конкретного файла на диске.
     *
     * @param file файл YAML
     * @return заполненная {@link PageModel}
     * @throws RuntimeException если чтение или парсинг файла завершился ошибкой
     */
    public PageModel load(File file) {
        try (InputStream is = new FileInputStream(file)) {
            return parseYaml(is, file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load YAML page file: " + file.getPath(), e);
        }
    }

    /**
     * Центральный парсер YAML -> PageModel.
     *
     * @param is       входной поток YAML (будет прочитан)
     * @param sourceId идентификатор источника (пусть для сообщений об ошибках)
     * @return PageModel
     * @throws IllegalStateException если структура YAML неверная
     */
    @SuppressWarnings("unchecked")
    private PageModel parseYaml(InputStream is, String sourceId) {
        Yaml yaml = new Yaml();
        Object root = yaml.load(is);

        if (root == null) {
            throw new IllegalStateException("YAML is empty: " + sourceId);
        }

        Map<?, ?> map = asMap(root);
        PageModel model = new PageModel();

        // --- PageInfo ---
        if (map.containsKey("PageInfo")) {
            Map<?, ?> infoMap = asMap(map.get("PageInfo"));
            PageInfo info = new PageInfo();
            if (infoMap.get("project") != null) info.setProject(String.valueOf(infoMap.get("project")));
            if (infoMap.get("feature") != null) info.setFeature(String.valueOf(infoMap.get("feature")));
            if (infoMap.get("name") != null) info.setName(String.valueOf(infoMap.get("name")));
            if (infoMap.get("url") != null) info.setUrl(String.valueOf(infoMap.get("url")));
            model.setPageInfo(info);
        }

        // --- Elements ---
        Map<String, ElementDefinition> elements;
        if (map.containsKey("elements")) {
            elements = toElementDefinitions(asMap(map.get("elements")));
        } else {
            // Если нет ключа elements, считаем, что элементы на корневом уровне (кроме PageInfo)
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

    /**
     * Загружает ресурс из classpath.
     *
     * @param path относительный путь
     * @return {@link InputStream} или null, если не найден
     */
    private InputStream getResource(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * Преобразует карту сырых данных из YAML в словарь имя -> ElementDefinition.
     * Поддерживает:
     * - плоский формат: locatorType + locatorValue
     * - вложенный объект locator { by, value }
     * - необязательное поле type
     *
     * @param raw карта из YAML
     * @return словарь ElementDefinition
     */
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

            // --- Локатор ---
            Object lt = def.get("locatorType");
            Object lv = def.get("locatorValue");

            // Вложенный блок locator
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

    /**
     * Проверяет, что объект является Map и безопасно приводит его к Map<?, ?>.
     *
     * @param obj объект
     * @return приведённая карта
     * @throws IllegalStateException если объект не является картой
     */
    @SuppressWarnings("unchecked")
    private Map<?, ?> asMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<?, ?>) obj;
        }
        throw new IllegalStateException("Invalid YAML structure: expected mapping, got " + obj);
    }
}
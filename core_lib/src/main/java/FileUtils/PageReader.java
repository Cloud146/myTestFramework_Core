package FileUtils;

import ErrorUtils.ErrorBuilder;
import config.RuntimeReader;
import data.ElementDefinition;
import data.PageModel;
import locator.Locator;
import logging.Log;
import org.slf4j.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Утилитарный класс для работы с уже загруженной YAML-страницей.
 *
 * Поиск и загрузка страниц основаны на имени из PageInfo.name и каталоге, указанном в runtime.yaml (ключ "pages_dir").
 * Класс предоставляет удобные геттеры для использования в тестах и в Allure Utils:
 * - getPageName(), getPageUrl(), getProject(), getFeature(), getPagePath()
 * - getElementDefinition(name), getLocator(name), getSemanticType(name)
 *
 * Исключения бросаются заранее при некорректной конфигурации (отсутствует PageInfo, отсутствует url или нет элементов).
 */
public class PageReader {

    private final String project;
    private final String feature;
    private final String pageName;
    private final String pageUrl;
    private final Map<String, ElementDefinition> elements;
    private final String pagePath;

    private static final Logger log = Log.get(PageReader.class);

    private PageReader(File yamlFile) {
        this.pagePath = yamlFile.getAbsolutePath();
        PageModel model = new YamlPageLoader().load(yamlFile);

        if (model.getPageInfo() == null) {
            throw new IllegalStateException("Missing PageInfo in: " + pagePath);
        }
        this.pageName = model.getPageInfo().getName();
        this.pageUrl  = model.getPageInfo().getUrl();
        this.project  = model.getPageInfo().getProject();
        this.feature  = model.getPageInfo().getFeature();

        if (pageUrl == null || pageUrl.isBlank()) {
            throw new IllegalStateException("Missing PageInfo.url in: " + pagePath);
        }

        this.elements = Optional.ofNullable(model.getElements()).orElseGet(Collections::emptyMap);
        if (elements.isEmpty()) {
            throw new IllegalStateException("No elements defined in: " + pagePath);
        }
    }

    /**
     * Найти страницу по PageInfo.name в каталоге из runtime.yaml (ключ "pages_dir").
     *
     * @param pageName значение PageInfo.name в YAML (поиск нечувствителен к регистру и пробелам)
     * @return PageReader для найденной страницы
     */
    public static PageReader usePageYAML(String pageName) {
        String baseDirStr = RuntimeReader.getString("pages_dir");

        log.trace("Директория файлов с описанием страниц: {}", baseDirStr);
        log.trace("Полный путь к папке с файлами описания страниц: {}", Paths.get(baseDirStr).toAbsolutePath());
        log.trace("Используется страница YAML: {}", pageName);

        if (baseDirStr == null || baseDirStr.isBlank()) {
            throw new IllegalStateException("runtime.yaml: missing 'pages_dir'");
        }
        Path baseDir = Paths.get(baseDirStr);

        try (Stream<Path> files = Files.walk(baseDir)) {
            List<File> matches = files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".yaml") || p.toString().endsWith(".yml"))
                    .map(Path::toFile)
                    .filter(f -> {
                        try {
                            PageModel m = new YamlPageLoader().load(f);
                            return m.getPageInfo() != null &&
                                    normalize(m.getPageInfo().getName()).equals(normalize(pageName));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            if (matches.isEmpty()) {
                throw new IllegalStateException("Page not found by name: " + pageName);
            }
            if (matches.size() > 1) {
                throw new IllegalStateException("Multiple pages found by name: " + pageName);
            }
            return new PageReader(matches.get(0));
        } catch (IOException e) {
            throw new RuntimeException("Error searching pages directory", e);
        }
    }

    /** Вернуть URL страницы из PageInfo.url */
    public String getPageUrl() {
        return pageUrl;
    }

    /** Вернуть имя страницы из PageInfo.name */
    public String getPageName() {
        return pageName;
    }

    /** Вернуть название проекта из PageInfo.project */
    public String getProject() {
        return project;
    }

    /** Вернуть название фичи из PageInfo.feature */
    public String getFeature() {
        return feature;
    }

    /** Вернуть путь к YAML-файлу на диске */
    public String getPagePath() {
        return pagePath;
    }

    /** Вернуть определение элемента по ключу из elements */
    public ElementDefinition getElementDefinition(String name) {
        ElementDefinition def = elements.get(name);
        if (def == null) {
            throw new IllegalArgumentException("Element not found: '" + name);
        }
        return def;
    }

    /** Вернуть универсальный локатор для элемента */
    public Locator getLocator(String name) {
        ElementDefinition def = getElementDefinition(name);

        if (def.getLocatorValue() == null || def.getLocatorValue().isBlank()) {
            ErrorBuilder.forAction("parse locator value")
                    .selector(name)
                    .message("Пустой локатор для элемента: " + name)
                    .raise();
        }
        return new Locator(def.toLocatorTypeEnum(), def.getLocatorValue());
    }

    /** Опционально: вернуть семантику элемента (type), если нужна в тестах/логах */
    public String getSemanticType(String name) {
        ElementDefinition def = getElementDefinition(name);
        String t = def.getType();
        return (t != null && !t.isBlank()) ? t : "generic";
    }

    private static String normalize(String s) {
        return s == null ? "" : s.replaceAll("\\s+", "").toLowerCase();
    }
}

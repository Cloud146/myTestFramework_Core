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
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class PageReader {

    private final String project;
    private final String feature;
    private final String pageName;
    private final String pageUrl;
    private final String pagePath;

    private static final Logger log = Log.get(PageReader.class);

    private final Map<String, ElementDefinition> elements;
    private static final Map<String, File> PAGE_CACHE = new ConcurrentHashMap<>();
    private static volatile boolean cacheInitialized = false;
    private static final Object LOCK = new Object();

    private PageReader(File yamlFile) {
        this.pagePath = yamlFile.getAbsolutePath();
        PageModel model = new YamlPageLoader().load(yamlFile);

        if (model.getPageInfo() == null) {
            throw new IllegalStateException("PageInfo не найдено по пути: " + pagePath);
        }
        this.pageName = model.getPageInfo().getName();
        this.pageUrl  = model.getPageInfo().getUrl();
        this.project  = model.getPageInfo().getProject();
        this.feature  = model.getPageInfo().getFeature();

        if (pageUrl == null || pageUrl.isBlank()) {
            throw new IllegalStateException("PageInfo.url  не найдено по пути: " + pagePath);
        }

        this.elements = Optional.ofNullable(model.getElements()).orElseGet(Collections::emptyMap);
        if (elements.isEmpty()) {
            throw new IllegalStateException("Нет доступных элементов по пути: " + pagePath);
        }
    }

    public static PageReader usePageYAML(String pageName) {
        if (!cacheInitialized) {
            synchronized (LOCK) {
                if (!cacheInitialized) {
                    initPageCache();
                    cacheInitialized = true;
                }
            }
        }

        String normalizedName = normalize(pageName);
        File file = PAGE_CACHE.get(normalizedName);

        if (file == null) {
            throw new IllegalStateException("Не найдена страница с именем: '" + pageName + "' (искали в кэше по ключу: '" + normalizedName + "')");
        }

        log.trace("Используется страница YAML: {} (файл: {})", pageName, file.getName());
        return new PageReader(file);
    }

    private static void initPageCache() {
        String baseDirStr = RuntimeReader.getString("pages_dir");
        if (baseDirStr == null || baseDirStr.isBlank()) {
            throw new IllegalStateException("runtime.yaml: missing 'pages_dir'");
        }
        Path baseDir = PathResolver.resolve(baseDirStr);

        log.debug("Инициализация кэша страниц. Сканирование директории: {}", baseDir.toAbsolutePath());

        try (Stream<Path> files = Files.walk(baseDir)) {
            files.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".yaml") || p.toString().endsWith(".yml"))
                    .forEach(path -> {
                        try {
                            PageModel m = new YamlPageLoader().load(path.toFile());
                            if (m.getPageInfo() != null && m.getPageInfo().getName() != null) {
                                String key = normalize(m.getPageInfo().getName());

                                if (PAGE_CACHE.containsKey(key)) {
                                    File existing = PAGE_CACHE.get(key);
                                    throw new IllegalStateException(String.format(
                                            "Найдено несколько страниц с именем '%s':\n1) %s\n2) %s",
                                            m.getPageInfo().getName(), existing.getAbsolutePath(), path.toAbsolutePath()
                                    ));
                                }

                                PAGE_CACHE.put(key, path.toFile());
                            }
                        } catch (Exception e) {
                            log.warn("Пропущен файл при сканировании страниц (ошибка парсинга): {} -> {}", path, e.getMessage());
                        }
                    });

            log.debug("Кэширование завершено. Загружено страниц: {}", PAGE_CACHE.size());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка обнаружения директории страниц: " + baseDir, e);
        }
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getPageName() {
        return pageName;
    }

    public String getProject() {
        return project;
    }

    public String getFeature() {
        return feature;
    }

    public String getPagePath() {
        return pagePath;
    }

    public ElementDefinition getElementDefinition(String name) {
        ElementDefinition def = elements.get(name);
        if (def == null) {
            throw new IllegalArgumentException("Element not found: '" + name);
        }
        return def;
    }

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

    public String getSemanticType(String name) {
        ElementDefinition def = getElementDefinition(name);
        String t = def.getType();
        return (t != null && !t.isBlank()) ? t : "generic";
    }

    private static String normalize(String s) {
        return s == null ? "" : s.replaceAll("\\s+", "").toLowerCase();
    }
}

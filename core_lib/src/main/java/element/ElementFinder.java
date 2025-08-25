package element;

import data.ElementDefinition;
import data.PageModel;
import driver.DriverAdapter;
import exceptions.ElementNotFoundInRepoException;
import exceptions.InvalidLocatorException;
import loader.YamlPageLoader;
import locator.Locator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class ElementFinder {

    private final DriverAdapter driver;
    private final String pagePath;
    private final Map<String, ElementDefinition> elements;
    private final String pageName;
    private final String pageUrl;

    public ElementFinder(DriverAdapter driver, Class<?> testClass) {
        this.driver = Objects.requireNonNull(driver, "driver");

        this.pagePath = autoDetectYaml(testClass);

        // теперь грузим напрямую из файла
        PageModel model = new YamlPageLoader().load(new File(pagePath));
        this.elements = model.getElements();
        this.pageName = (model.getMeta() != null) ? model.getMeta().getName() : null;
        this.pageUrl  = (model.getMeta() != null) ? model.getMeta().getUrl()  : null;

        if (elements == null || elements.isEmpty()) {
            throw new IllegalStateException("No elements defined in page: " + pagePath);
        }
    }

    public UIElement get(String name) {
        ElementDefinition def = elements.get(name);
        if (def == null) {
            throw new ElementNotFoundInRepoException(name, pagePath);
        }
        if (def.getLocatorType() == null || def.getLocatorValue() == null) {
            throw new InvalidLocatorException(
                    "Missing locatorType/locatorValue for '" + name + "' @ " + pagePath
            );
        }
        Locator locator = new Locator(def.toLocatorTypeEnum(), def.getLocatorValue());
        String semantic = (def.getType() != null && !def.getType().isBlank())
                ? def.getType()
                : "generic";
        return new UIElement(driver, locator, semantic);
    }

    public String getPageName() {
        return pageName;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    private String autoDetectYaml(Class<?> testClass) {
        Path testDir = Paths.get("src", "test", "java")
                .resolve(testClass.getPackageName().replace('.', File.separatorChar));
        String baseName = testClass.getSimpleName().replace("Test", "Page");

        Path yamlPath = testDir.resolve(baseName + ".yaml");
        if (!yamlPath.toFile().exists()) {
            yamlPath = testDir.resolve(baseName + ".yml");
        }
        if (!yamlPath.toFile().exists()) {
            throw new IllegalStateException("YAML page file not found: " + yamlPath);
        }
        return yamlPath.toString();
    }
}

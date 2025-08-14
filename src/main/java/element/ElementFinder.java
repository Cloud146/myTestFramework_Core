package element;

import annotations.PageFile;
import data.ElementDefinition;
import data.PageModel;
import driver.DriverAdapter;
import exceptions.ElementNotFoundInRepoException;
import exceptions.InvalidLocatorException;
import loader.YamlPageLoader;
import locator.Locator;

import java.util.Map;
import java.util.Objects;

public class ElementFinder {
    private final DriverAdapter driver;
    private final String pagePath;
    private final Map<String, ElementDefinition> elements;

    public ElementFinder(DriverAdapter driver, Class<?> pageClass) {
        this.driver = Objects.requireNonNull(driver);
        PageFile pf = pageClass.getAnnotation(PageFile.class);
        if (pf == null) {
            throw new IllegalStateException("@PageFile missing on " + pageClass.getName());
        }
        this.pagePath = pf.value();
        PageModel model = new YamlPageLoader().load(pagePath);
        this.elements = model.getElements();
    }

    public UIElement get(String name) {
        ElementDefinition def = elements.get(name);
        if (def == null) throw new ElementNotFoundInRepoException(name, pagePath);
        if (def.getLocatorType() == null || def.getLocatorValue() == null) {
            throw new InvalidLocatorException("Missing locatorType/locatorValue for '" + name + "'");
        }
        var locator = new Locator(def.toLocatorTypeEnum(), def.getLocatorValue());
        String semantic = def.getType() != null ? def.getType() : "generic";
        return new UIElement(driver, locator, semantic);
    }
}

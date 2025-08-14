package loader;

import config.FrameworkConfig;
import data.PageModel;
import exceptions.PageFileNotFoundException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public final class YamlPageLoader {

    public PageModel load(String pagePath) {
        String base = FrameworkConfig.pagesBasePath();
        String resource = base + "/" + pagePath + ".yaml";

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        if (is == null) {
            throw new PageFileNotFoundException("YAML page file not found: " + resource);
        }

        Yaml yaml = new Yaml();
        return yaml.loadAs(is, PageModel.class);
    }
}

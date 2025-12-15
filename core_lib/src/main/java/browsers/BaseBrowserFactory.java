package browsers;

import logging.Log;
import org.slf4j.Logger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public abstract class BaseBrowserFactory<T> implements BrowserFactory<T> {

    protected final Logger log = Log.get(getClass());

    protected String getAbsolutePathFromResources(String folder, String fileName) {
        if (fileName == null || fileName.isBlank()) return null;

        String resourcePath = folder + fileName;
        URL resource = Thread.currentThread().getContextClassLoader().getResource(resourcePath);

        if (resource != null) {
            try {
                return Paths.get(resource.toURI()).toFile().getAbsolutePath();
            } catch (URISyntaxException e) {
                log.error("[BaseFactory] Ошибка преобразования URI ресурса: {}", resourcePath, e);
            }
        }
        return null;
    }

    protected int[] parseWindowSize(String mode) {
        if (mode == null || !mode.startsWith("size:")) return null;

        try {
            String raw = mode.substring(5); // убираем "size:"
            String[] parts = raw.split("x");
            if (parts.length == 2) {
                int w = Integer.parseInt(parts[0].trim());
                int h = Integer.parseInt(parts[1].trim());
                return new int[]{w, h};
            }
        } catch (Exception e) {
            log.warn("[BaseFactory] Некорректный формат размера окна: '{}'. Ожидается size:WxH", mode);
        }
        return null;
    }
}

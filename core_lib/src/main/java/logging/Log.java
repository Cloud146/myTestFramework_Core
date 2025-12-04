package logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {

    private Log() {}

    public static Logger get(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static org.slf4j.Logger get(String name) {
        return LoggerFactory.getLogger(name);
    }
}

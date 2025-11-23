package logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public final class Log {

    private Log() {}

    public static Logger get(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static org.slf4j.Logger get(String name) {
        return LoggerFactory.getLogger(name);
    }

//    public static void cucumber(Logger log, String message, Object... args) {
//        MDC.put("customLevel", "CUCUMBER");
//        log.info(message, args);
//        MDC.remove("customLevel");
//    }
}

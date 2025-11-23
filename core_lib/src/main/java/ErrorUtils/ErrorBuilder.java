package ErrorUtils;

import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public final class ErrorBuilder {
    private String action;
    private String selector;
    private String url;
    private Throwable cause;
    private String customMessage;

    private ErrorBuilder() {}

    public static ErrorBuilder forAction(String action) {
        ErrorBuilder b = new ErrorBuilder();
        b.action = action;
        return b;
    }

    public ErrorBuilder selector(String selector) {
        this.selector = selector;
        return this;
    }

    public ErrorBuilder url(String url) {
        this.url = url;
        return this;
    }

    public ErrorBuilder causedBy(Throwable t) {
        this.cause = t;
        return this;
    }

    public ErrorBuilder message(String msg) {
        this.customMessage = msg;
        return this;
    }

    public void raise() {
        if (cause != null) {
            String details = cause.toString() + "\n\n" + stackToString(cause);
            Allure.addAttachment("Error trace", "text/plain",
                    new ByteArrayInputStream(details.getBytes(StandardCharsets.UTF_8)), "txt");
        }

        String base = customMessage != null
                ? customMessage
                : String.format("%s failed on '%s' at '%s'",
                action,
                selector == null ? "<none>" : selector,
                url == null ? "<unknown>" : url);

        throw new FrameworkException(base, action, selector, url);
    }

    private static String stackToString(Throwable t) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement el : t.getStackTrace()) {
            sb.append(el.toString()).append("\n");
        }
        return sb.toString();
    }
}

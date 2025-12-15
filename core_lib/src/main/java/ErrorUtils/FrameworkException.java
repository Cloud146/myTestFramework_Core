package ErrorUtils;

public class FrameworkException extends RuntimeException {
    private final String action;
    private final String selector;
    private final String url;

    public FrameworkException(String message, String action, String selector, String url) {
        super(message);
        this.action = action;
        this.selector = selector;
        this.url = url;
    }

    public String getAction() { return action; }
    public String getSelector() { return selector; }
    public String getUrl() { return url; }
}

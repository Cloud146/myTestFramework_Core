package browsers;

public interface BrowserFactory<T> {

    String getBrowserName();

    T createDriver(BrowserConfig config);
}

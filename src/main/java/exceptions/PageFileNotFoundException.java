package exceptions;

public class PageFileNotFoundException extends RuntimeException {
    public PageFileNotFoundException(String message) { super(message); }
}

package exceptions;

public class ElementNotFoundInRepoException extends RuntimeException {
    public ElementNotFoundInRepoException(String elementName, String pagePath) {
        super("Element '" + elementName + "' not found in page repo: " + pagePath);
    }
}

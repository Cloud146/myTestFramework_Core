package exceptions;

public class InvalidLocatorException extends RuntimeException {
    public InvalidLocatorException(String reason) { super(reason); }
}

package exceptions;

/**
 * Ошибка: локатор отсутствует, некорректен или имеет неподдерживаемый тип.
 */
public class InvalidLocatorException extends RuntimeException {
    public InvalidLocatorException(String reason) { super(reason); }
}

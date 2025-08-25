package exceptions;

/**
 * Ошибка: YAML-файл страницы не найден в classpath по ожидаемому пути.
 */
public class PageFileNotFoundException extends RuntimeException {
    public PageFileNotFoundException(String message) { super(message); }
}

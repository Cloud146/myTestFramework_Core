package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PageFile {
    /**
     * Путь к YAML без расширения относительно basePath (по умолчанию "pages").
     * Пример: "android/LoginPage"
     */
    String value();
}
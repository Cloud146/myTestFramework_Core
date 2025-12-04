package TestUtils.JavaParallel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ParallelClass {

    /**
     * Количество потоков (threadPoolSize) для @Test методов в классе.
     * По умолчанию 0 — означает "авто" (число методов или доступные CPU).
     */
    int threads() default 0;
}

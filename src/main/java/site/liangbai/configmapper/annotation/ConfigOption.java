package site.liangbai.configmapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigOption {
    /**
     * The configuration key name.
     * @return String
     */
    String value();

    /**
     * The configuration section.
     * @return String
     */
    String section() default "";
}

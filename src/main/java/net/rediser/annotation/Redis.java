package net.rediser.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Redis {

    String name();
    String hostName() default "localhost";

    String password() default "unspecified";
    int port() default 6379;
}

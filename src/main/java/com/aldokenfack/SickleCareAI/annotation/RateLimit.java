package com.aldokenfack.SickleCareAI.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    int attempts() default 5;
    int period() default 1;
    TimeUnit unit() default TimeUnit.MINUTES;
    // Key: "IP" or "IP+USER" or "SUBNET+USER"
    String keyType() default "IP";

}

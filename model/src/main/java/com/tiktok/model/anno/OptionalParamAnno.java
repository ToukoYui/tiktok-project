package com.tiktok.model.anno;


import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalParamAnno {
}

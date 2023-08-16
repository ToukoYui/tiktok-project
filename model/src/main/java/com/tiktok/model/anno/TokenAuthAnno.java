package com.tiktok.model.anno;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 运行时有效
 * 可以作用与方法和参数上
 */

@Documented
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenAuthAnno {
}

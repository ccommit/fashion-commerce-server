package com.ccommit.fashionserver.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * packageName    : com.ccommit.fashionserver.aop
 * fileName       : LoginCheck
 * author         : juoiy
 * date           : 2023-09-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-22        juoiy       최초 생성
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginCheck {
    public static enum UserType {
        USER, ADMIN, SELLER
    }

    UserType[] types() default {};
}

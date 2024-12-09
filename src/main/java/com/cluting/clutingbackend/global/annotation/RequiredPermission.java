package com.cluting.clutingbackend.global.annotation;


import com.cluting.clutingbackend.global.enums.PermissionLevel;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    PermissionLevel value();
}

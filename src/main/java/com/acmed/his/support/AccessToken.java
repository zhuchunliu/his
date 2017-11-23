package com.acmed.his.support;

import java.lang.annotation.*;

/**
 * Created by Darren on 2017-11-23
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessToken {

    boolean required() default true;
}

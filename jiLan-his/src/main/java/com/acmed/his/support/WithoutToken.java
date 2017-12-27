package com.acmed.his.support;

/**
 * Created by Darren on 2017-12-06
 **/

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithoutToken {
}

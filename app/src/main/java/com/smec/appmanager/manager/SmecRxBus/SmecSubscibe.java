package com.smec.appmanager.manager.SmecRxBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xupeizuo on 2018/5/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SmecSubscibe {
    
    SubscribeTargets value() ;
}

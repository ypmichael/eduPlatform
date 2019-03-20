package com.third.IntelPlat.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelResources {
  //声明策略的值
  String title();
  //声明一个order值,并且其默认值为9999
  int order() default 9999;
}

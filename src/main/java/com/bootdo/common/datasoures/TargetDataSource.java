package com.bootdo.common.datasoures;

import java.lang.annotation.*;

/**
 * 
    * @ClassName: TargetDataSource
    * @Description: TODO(在方法上使用，用于指定使用哪个数据源)
    * @author Andrew
    * @date 2018年5月8日
    *
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}
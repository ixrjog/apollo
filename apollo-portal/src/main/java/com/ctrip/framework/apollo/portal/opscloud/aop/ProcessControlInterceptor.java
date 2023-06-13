package com.ctrip.framework.apollo.portal.opscloud.aop;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2023/5/26 17:33
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ProcessControlInterceptor {

    String url() default "https://oc.chuanyinet.com";

    String token() default "";

    String appIdSpEL() default "";

    String envSpEL() default "";

    String clusterNameSpEL() default "";

    String namespaceNameSpEL() default "";

    // 灰度发布
    String branchNameSpEL() default "";

    String releaseTitleSpEL() default "";

    String releaseCommentSpEL() default "";

    String releasedBySpEL() default "";

    String isEmergencyPublishSpEL() default "";

}

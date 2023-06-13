package com.ctrip.framework.apollo.portal.opscloud.param;

import lombok.*;

/**
 * @Author baiyi
 * @Date 2023/5/29 09:35
 * @Version 1.0
 */
public class OpscloudParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ReleaseEvent {

        private String appId;

        private String env;

        private String username;

        private String clusterName;

        private String namespaceName;

        private String branchName;

        private String token;

        private Boolean isGray;

    }

}

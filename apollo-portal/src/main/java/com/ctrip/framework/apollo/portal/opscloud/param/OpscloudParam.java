package com.ctrip.framework.apollo.opscloud.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

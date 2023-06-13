package com.ctrip.framework.apollo.opscloud.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/5/29 11:05
 * @Version 1.0
 */
public class OpscloudResult {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterceptionEventResponse {

        private String body;

        private boolean success;

        private String msg;

        private int code;

    }

}

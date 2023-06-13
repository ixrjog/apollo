package com.ctrip.framework.apollo.opscloud.feign;

import com.ctrip.framework.apollo.opscloud.param.OpscloudParam;
import com.ctrip.framework.apollo.opscloud.result.OpscloudResult;
import feign.Headers;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2023/5/29 10:56
 * @Version 1.0
 */
public interface OpscloudV1Feign {

    @RequestLine("POST /apollo/interception/event")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    OpscloudResult.InterceptionEventResponse preInterceptionEvent(OpscloudParam.ReleaseEvent interceptionEvent);

}

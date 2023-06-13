package com.ctrip.framework.apollo.portal.opscloud.api;

import com.ctrip.framework.apollo.portal.opscloud.feign.OpscloudV1Feign;
import com.ctrip.framework.apollo.portal.opscloud.param.OpscloudParam;
import com.ctrip.framework.apollo.portal.opscloud.result.OpscloudResult;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/29 11:12
 * @Version 1.0
 */
@Component
public class OpscloudApi {

    public OpscloudResult.InterceptionEventResponse preInterceptionEvent(String url, OpscloudParam.ReleaseEvent interceptionEvent) {
        OpscloudV1Feign opscloudApi = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OpscloudV1Feign.class, url);
        return opscloudApi.preInterceptionEvent(interceptionEvent);
    }

}

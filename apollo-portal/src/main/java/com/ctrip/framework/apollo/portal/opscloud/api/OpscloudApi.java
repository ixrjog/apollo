package com.ctrip.framework.apollo.opscloud.api;

import com.ctrip.framework.apollo.opscloud.feign.OpscloudV1Feign;
import com.ctrip.framework.apollo.opscloud.param.OpscloudParam;
import com.ctrip.framework.apollo.opscloud.result.OpscloudResult;
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

    private final static String OC_URL = "https://oc.chuanyinet.com";

    public OpscloudResult.InterceptionEventResponse preInterceptionEvent(OpscloudParam.ReleaseEvent interceptionEvent) {
        OpscloudV1Feign opscloudApi = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OpscloudV1Feign.class, OC_URL);
        return opscloudApi.preInterceptionEvent(interceptionEvent);
    }

}

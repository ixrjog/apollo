package com.ctrip.framework.apollo.portal.opscloud.aop;

import com.ctrip.framework.apollo.common.exception.BadRequestException;
import com.ctrip.framework.apollo.portal.opscloud.api.OpscloudApi;
import com.ctrip.framework.apollo.portal.opscloud.param.OpscloudParam;
import com.ctrip.framework.apollo.portal.opscloud.result.OpscloudResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2023/5/29 09:22
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class ProcessControlInterceptorAspect {

    @Value("${opscloud.url}")
    private String url;

    @Value("${opscloud.token}")
    private String token;

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Resource
    private OpscloudApi opscloudApi;

    @Pointcut(value = "@annotation(com.ctrip.framework.apollo.portal.opscloud.aop.ProcessControlInterceptor)")
    public void annotationPoint() {
    }

    @Around("@annotation(processControlInterceptor)")
    public Object around(ProceedingJoinPoint joinPoint, ProcessControlInterceptor processControlInterceptor) throws Throwable {
        //获取切面方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法的形参名称
        String[] params = discoverer.getParameterNames(method);
        //获取方法的实际参数值
        Object[] arguments = joinPoint.getArgs();
        //设置解析SpEL所需的数据上下文
        EvaluationContext context = new StandardEvaluationContext();
        IntStream.range(0, Objects.requireNonNull(params).length).forEachOrdered(len -> context.setVariable(params[len], arguments[len]));
        //解析表达式并获取SpEL的值
        Expression appIdExpression = expressionParser.parseExpression(processControlInterceptor.appIdSpEL());
        Expression envExpression = expressionParser.parseExpression(processControlInterceptor.envSpEL());
        Expression clusterNameExpression = expressionParser.parseExpression(processControlInterceptor.clusterNameSpEL());
        Expression namespaceNameExpression = expressionParser.parseExpression(processControlInterceptor.namespaceNameSpEL());
        String branchName = "";
        if (StringUtils.isNotBlank(processControlInterceptor.branchNameSpEL())) {
            Expression branchNameExpression = expressionParser.parseExpression(processControlInterceptor.branchNameSpEL());
            branchName = Objects.toString(branchNameExpression.getValue(context), "");
        }
        try {
            String appId = Objects.requireNonNull(appIdExpression.getValue(context)).toString();
            String env = Objects.requireNonNull(envExpression.getValue(context)).toString();
            String clusterName = Objects.requireNonNull(clusterNameExpression.getValue(context)).toString();
            String namespaceName = Objects.requireNonNull(namespaceNameExpression.getValue(context)).toString();
            String username = getUsername();
            OpscloudParam.ReleaseEvent interceptionEventParam = OpscloudParam.ReleaseEvent.builder()
                    .username(username)
                    .appId(appId)
                    .env(env)
                    .clusterName(clusterName)
                    .namespaceName(namespaceName)
                    .branchName(branchName)
                    .isGray(StringUtils.isNotBlank(branchName))
                    .token(token)
                    .build();
            log.debug(interceptionEventParam.toString());
            handleApiInterceptionVerification(interceptionEventParam);
        } catch (NullPointerException e) {
            throw new BadRequestException("参数不正确, 这个错误不应该发生！");
        }
        return joinPoint.proceed();
    }

    /**
     * 从上下文中获取登录用户名
     * @return
     */
    private String getUsername(){
        // auth.getName(); // 登录用户名
        //LdapUserDetailsImpl user = (LdapUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth.getName();
        } catch (Exception e){
            throw new BadRequestException("获取用户名错误！");
        }
    }

    /**
     * 调用OC
     *
     * @param interceptionEventParam
     */
    private void handleApiInterceptionVerification(OpscloudParam.ReleaseEvent interceptionEventParam) {
        OpscloudResult.InterceptionEventResponse response;
        try {
            response = opscloudApi.preInterceptionEvent(url, interceptionEventParam);
            if (!response.isSuccess()) {
                throw new AccessDeniedException(MessageFormatter.format("Access is denied: {}",response.getMsg()).getMessage());
            }
        } catch (Exception e) {
            throw new BadRequestException(MessageFormatter.format("OC接口错误: {}",e.getMessage()).getMessage());
        }
    }

}

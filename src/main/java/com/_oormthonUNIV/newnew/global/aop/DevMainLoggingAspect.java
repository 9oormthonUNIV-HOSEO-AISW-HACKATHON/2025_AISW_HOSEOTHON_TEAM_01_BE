package com._oormthonUNIV.newnew.global.aop;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class DevMainLoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // === Controller ===
    @Pointcut("execution(* com._oormthonUNIV.newnew..controller..*(..))")
    private void controllerLayer() {
    }

    // === Facade ===
    @Pointcut("execution(* com._oormthonUNIV.newnew..facade..*(..))")
    private void facadeLayer() {
    }

    // === Service ===
    @Pointcut("execution(* com._oormthonUNIV.newnew..service..*(..))")
    private void serviceLayer() {
    }

    // === Repository ===
    @Pointcut("execution(* com._oormthonUNIV.newnew..repository..*(..))")
    private void repositoryLayer() {
    }

    // === Controller 로그 ===
    @Around("controllerLayer()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logWithLayer(
                "[" + joinPoint.getSignature().getDeclaringType().getSimpleName() + "]",
                joinPoint
        );
    }

    // === Facade 로그 ===
    @Around("facadeLayer()")
    public Object logFacade(ProceedingJoinPoint joinPoint) throws Throwable {
        return logWithLayer(
                "[" + joinPoint.getSignature().getDeclaringType().getSimpleName() + "]",
                joinPoint
        );
    }

    // === Service 로그 ===
    @Around("serviceLayer()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logWithLayer(
                "[" + joinPoint.getSignature().getDeclaringType().getSimpleName() + "]",
                joinPoint
        );
    }

    // === Repository 로그 ===
    @Around("repositoryLayer()")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        return logWithLayer(
                "[" + joinPoint.getSignature().getDeclaringType().getSimpleName() + "]",
                joinPoint
        );
    }

    // === 공통 로깅 로직 ===
    private Object logWithLayer(String layer, ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodInfo = signature.getDeclaringTypeName() + "." + signature.getName();

        // --- 인자 ---
        String argsString = sanitizeArgs(joinPoint.getArgs());
        log.info("{} [START] {} args={}", layer, methodInfo, argsString);

        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            String resultString = formatResult(result);
            log.info("{} [END] {} result={} ({}ms)", layer, methodInfo, resultString, elapsed);
            return result;
        } catch (Throwable e) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("{} [EXCEPTION] {} message={} ({}ms)", layer, methodInfo, e.getMessage(), elapsed);
            throw e;
        }
    }

    // === 민감정보 필터링 ===
    private String sanitizeArgs(Object[] args) {
        return Arrays.toString(Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return "null";

                    if (arg instanceof String s) {
                        String lower = s.toLowerCase();
                        if (lower.contains("bearer")) return "[MASKED_TOKEN]";
                        if (lower.contains("password")) return "[MASKED_PASSWORD]";
                        return s;
                    }

                    try {
                        String json = objectMapper.writeValueAsString(arg);
                        json = json.replaceAll(
                                "(?i)\"password\"\\s*:\\s*\"[^\"]*\"",
                                "\"password\":\"[MASKED_PASSWORD]\""
                        );
                        json = json.replaceAll(
                                "(?i)\"authorization\"\\s*:\\s*\"Bearer [^\"]*\"",
                                "\"authorization\":\"Bearer [MASKED_TOKEN]\""
                        );
                        return json;
                    } catch (Exception e) {
                        return arg.toString();
                    }
                })
                .toArray());
    }

    // === 결과 문자열 길이 제한 ===
    private String formatResult(Object result) {
        if (result == null) return "null";
        try {
            String json = objectMapper.writeValueAsString(result);
            return json.length() > 500 ? json.substring(0, 500) + "...(truncated)" : json;
        } catch (JsonProcessingException e) {
            String s = result.toString();
            return s.length() > 200 ? s.substring(0, 200) + "...(truncated)" : s;
        }
    }
}

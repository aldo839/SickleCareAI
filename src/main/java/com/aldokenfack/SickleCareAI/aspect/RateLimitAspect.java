package com.aldokenfack.SickleCareAI.aspect;

import com.aldokenfack.SickleCareAI.annotation.RateLimit;
import com.aldokenfack.SickleCareAI.service.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RateLimitService rateLimitService;

    @Around("@annotation(rateLimit)")
    public Object limit(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // Fetch user IP Address
        String xff = request.getHeader("X-Forwarded-For");
        String ip = (xff != null && !xff.isBlank()? xff.split(",")[0].trim() : request.getRemoteAddr());

        // Fetch user email
        String email = request.getParameter("email");
        if (email == null) email = "anonymous";

        // Construct a key by the type
        String key;

        if ("IP+USER".equals(rateLimit.keyType())){
            key = ip + ":" + email;

        } else if ("SUBNET+USER".equals(rateLimit.keyType())) {
            key = rateLimitService.getSubnet(ip) + ":" + email;

        } else {
            key = ip; // By default
        }

        String finalKey = pjp.getSignature().toShortString() + ":" + key;

        // Bucket
        Duration period = Duration.of(rateLimit.period(), rateLimit.unit().toChronoUnit());
        Bucket bucket = rateLimitService.getBucket(finalKey, rateLimit.attempts(), period);

        if (bucket.tryConsume(1)){
            return pjp.proceed();
        } else {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

}

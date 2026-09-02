package com.aldokenfack.SickleCareAI.config;

import com.aldokenfack.SickleCareAI.service.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(1) // To be the first to run
@RequiredArgsConstructor
public class GlobalRateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // We don't rate-limited the login because he already has his rate-limit
        if (request.getRequestURI().contains("/login")){
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();
        String key = "GLOBAL:" + ip;

        // 200 requests per minutes for everyone
        Bucket bucket = rateLimitService.getBucket(key, 200, Duration.ofMinutes(1));

        if (bucket.tryConsume(1)){
            filterChain.doFilter(request, response);

        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.setHeader("Retry-After", "60"); // Give a waiting time to client (60s)
            response.setHeader("X-Rate-Limit-Remaining", "0");

            String json = """
                    {
                    "status": 429,
                    "error": "Too Many Requests",
                    "message": "Response limit, Please try back after one minute."
                    }
                    """;
            response.getWriter().write(json);
        }
    }
}

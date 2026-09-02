package com.aldokenfack.SickleCareAI.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket getBucket(String key, int attempts, Duration period){
        return buckets.computeIfAbsent(key, k -> {
            Refill refill = Refill.greedy(attempts, period);
            Bandwidth limit = Bandwidth.classic(attempts, refill);
            return Bucket.builder().addLimit(limit).build();
        });
    }

    public String getSubnet(String ip){

        if (ip==null) return "unknow";
        if (ip.contains(".")){
            try {
                return ip.substring(0, ip.lastIndexOf('.')) + ".0/24";
            } catch (Exception e){
                return ip;
            }
        }
        return ip;
    }

}

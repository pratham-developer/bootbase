package com.pratham.bootbase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private static final String keyPrefix = "jwt:blacklist:";
    private final RedisTemplate<String,Object> redisTemplate;

    public void blacklistAccessToken(String jti, Long expirationTime){
        //key is prefix + jti
        String key = keyPrefix+jti;
        long ttl = expirationTime - System.currentTimeMillis();
        if(ttl>0){
            //set in redis, key -> value(1)
            redisTemplate.opsForValue().set(
                    key,"1",ttl, TimeUnit.MILLISECONDS
            );
        }
    }

    public boolean isAccessTokenBlacklisted(String jti){
        //token is blacklisted if key containing its jti is present
        return redisTemplate.hasKey(keyPrefix+jti);
    }



}

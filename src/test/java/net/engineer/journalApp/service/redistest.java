package net.engineer.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class redistest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled
    @Test
    void abc(){
        redisTemplate.opsForValue().set("email","rohan2201saha@gmail.com");
        Object email = redisTemplate.opsForValue().get("email");
        int a = 1;
    }
}

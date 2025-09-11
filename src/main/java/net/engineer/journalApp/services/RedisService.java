package net.engineer.journalApp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entitiyClass){
        try{
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null) {  // prevent NullPointerException
                return null;
            }
            ObjectMapper om = new ObjectMapper();
            return om.readValue(o.toString(),entitiyClass);
        }
        catch (Exception e){
            log.error("Exception+ ",e);
            return null;
        }
    }
    public void set(String key,Object o,Long ttl){
        try{
            if (o == null) {
                log.warn("Attempted to cache null object for key {}", key);
                return;
            }
            ObjectMapper obj = new ObjectMapper();
            String Json = obj.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,Json,ttl, TimeUnit.SECONDS);
        }
        catch (Exception e){
            log.error("Exception+ ",e);
        }
    }

}

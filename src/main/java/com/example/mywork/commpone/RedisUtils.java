package com.example.mywork.commpone;

import com.example.mywork.service.impl.EmailCodeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils<K,V> {
    @Resource
    private RedisTemplate<K,V> redisTemplate;

    //调试日志
    private final static Logger logger = LoggerFactory.getLogger(RedisUtils.class);


    /**
     * 删除缓存
     */
    public void delete(String...key){
        if (key!=null&&key.length>0){
            if (key.length==1) {
                redisTemplate.delete((K) key[0]);
            }else {
                redisTemplate.delete((Collection<K>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 获取缓存
     */
    public V get(String key){
        return key!=null?redisTemplate.opsForValue().get(key):null;
    }

    /**
     * 设置时间
     * @param key
     * @param value
     * @param time
     * @return
     */

    public boolean setex(K key,V value,long time){
        if (time>0){
            redisTemplate.opsForValue().set( key,value,time, TimeUnit.SECONDS);
        }else {
            redisTemplate.opsForValue().set(key,value);
        }return true;
    }

    public boolean set(K key,V value){
        redisTemplate.opsForValue().set(key,value);
        return true;
    }


}

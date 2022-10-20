package yxl.testapp.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Boolean findKey(String un, String pwd) {
        boolean hasKey = redisTemplate.hasKey(un);
        if (hasKey) {
            String pwd1 = redisTemplate.opsForValue().get(un);
            if (pwd.equals(pwd1)) {
                hasKey = true;
            }
        }
        return hasKey;
    }

    public void insertKey(String s1, String s2) {
        redisTemplate.opsForValue().set(s1, s2, 5, TimeUnit.MINUTES);
    }

    public String findKey(String s1) {
        String s = redisTemplate.opsForValue().get(s1);
        return s;
    }

}

package yxl.testapp.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import pto.TestProto;
import yxl.testapp.logs.OptionDetails;


@Component
public class RedisUtil {


    @Autowired
    private RedisTemplate redisTemplate;

    ValueOperations<String, String> operations = redisTemplate.opsForValue();

    public Boolean findKey(String un,String pwd) {
        boolean hasKey = redisTemplate.hasKey(un);
        if (hasKey) {
            String pwd1 = operations.get(un);
            if (pwd.equals(pwd1)) {
                hasKey=true;
            }
        }
        return hasKey;
    }

    public void insterKey(String s1,String s2){
        operations.set(s1,s2);
    }
    public String findKey(String s1) {

        String s = operations.get(s1);
        return s;
    }

}

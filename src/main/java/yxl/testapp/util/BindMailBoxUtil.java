package yxl.testapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestParam;
import pto.TestProto;
import yxl.testapp.logs.OptionDetails;

import java.util.HashMap;
import java.util.Random;

public class BindMailBoxUtil {

    private Random random = new Random();


    //可换成redis，存储验证码
//     public static HashMap<String, String> emails = new HashMap<>();




    //随机生成验证码
    public String randomCode() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 6; i++)
            res.append(random.nextInt(10));

        return res.toString();
    }


    //发送验证码
    public Boolean sendEMail(String email, String code, JavaMailSender mailSender, RedisTemplate redisTemplate) {


        ValueOperations<String, String> operations = redisTemplate.opsForValue();
//        this.emails.put(email, code);
        operations.set(email,code);
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("l17660687587@163.com"); //设置发送邮件账号
            simpleMailMessage.setTo(email); //设置接收邮件的人，可以多个
            simpleMailMessage.setSubject("验证码"); //设置发送邮件的主题，就是标题
            simpleMailMessage.setText(code); //设置发送邮件的内容
            mailSender.send(simpleMailMessage);
            System.out.println("邮件发送成功!");
            return true;
        } catch (MailException e) {
            System.out.println(e);
            System.out.println("邮件发送失败!");
            return false;
        }
    }

    //验证邮箱验证码
//    public boolean checkCode(@RequestParam("e") String email, @RequestParam("c") String code) {
    public Boolean checkCode(String email, String code ,RedisTemplate redisTemplate) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String c=operations.get(email);
        if(c.equals(code)){
            return true;
        }else {
            return false;
        }
       /* String c = this.emails.get(email);
        if (c == null)
            return false;
        if (c.equals(code))
            return true;
        else
            return false;*/
    }
}
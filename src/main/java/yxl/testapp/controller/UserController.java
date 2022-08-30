package yxl.testapp.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author yxl
 * @date: 2022/8/30 上午11:56
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public byte[] login(@RequestBody byte[] data){
        return new byte[100];
    }

    @GetMapping("/")
    @ResponseBody
    public String test(){
        return "hello";
    }

}

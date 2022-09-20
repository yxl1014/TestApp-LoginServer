package yxl.testapp.service;

import proto.TestProto;

/**
 * @author yxl
 * @date: 2022/9/12 下午2:49
 * 用户Service类
 */
public interface UserService {

    /**
     * 登录逻辑
     * */
    byte[] login(byte[] data);

    boolean checkUser(TestProto.User user);

    byte[] register(byte[] data);
}

package yxl.testapp.service;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author yxl
 * @date: 2022/9/12 下午2:49
 * 用户Service类
 */
public interface UserService {

    /**
     * 登录逻辑
     * */
    byte[] login(byte[] data) throws InvalidProtocolBufferException;
}
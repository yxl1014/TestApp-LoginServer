package yxl.testapp.service.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pto.TestProto;
import yxl.testapp.logs.LogMsg;
import yxl.testapp.logs.LogUtil;
import yxl.testapp.logs.OptionDetails;
import yxl.testapp.mapper.UserMapper;
import yxl.testapp.service.UserService;
import yxl.testapp.util.FinalData;
import yxl.testapp.util.JWTUtil;
import yxl.testapp.util.ProtocolUtil;

/**
 * @author yxl
 * @date: 2022/9/12 下午2:50
 */

@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LogUtil.getLogger(UserServiceImpl.class);

    @Autowired
    private ProtocolUtil protocolUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public byte[] login(byte[] data) {
        TestProto.S2C_Login.Builder result = TestProto.S2C_Login.newBuilder();
        TestProto.C2S_Login builder = null;
        try {
            builder = TestProto.C2S_Login.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, OptionDetails.PROTOBUF_ERROR));

            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
        }

        int type = builder.getLoginType();

        String un;
        String pwd = builder.getUser().getUserPassword();
        TestProto.User user;
        switch (type) {
            case 0: {
                un = builder.getUser().getUserTel();
                user = userMapper.findUserByTelAndPwd(un, pwd);
                break;
            }
            case 1: {
                un = builder.getUser().getUserEmail();
                user = userMapper.findUserByTelAndPwd(un, pwd);
                break;
            }
            default:
                logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, OptionDetails.PARAM_ERROR));
                result.setStatus(false);
                result.setMsg(OptionDetails.PARAM_ERROR.getMsg());
                byte[] bytes = result.buildPartial().toByteArray();
                return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
        }
        if (user == null) {
            //日志
            OptionDetails log = type == 0 ? OptionDetails.LOGIN_TEL_PWD_ERROR :
                    OptionDetails.LOGIN_EMAIL_PWD_ERROR;
            logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, log));

            result.setStatus(false);
            result.setMsg(log.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, OptionDetails.LOGIN_OK, user));

            //ip地址出现变化，修改数据库
            if (!user.getUserIp().equals(builder.getUser().getUserIp())) {
                logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, OptionDetails.UPDATE_IP));
                userMapper.updateUserIPByUserId(builder.getUser().getUserIp(), user.getUserId());
                userMapper.updateUserPosByUserId(builder.getUser().getUserPos(), user.getUserId());
            }

            result.setStatus(true);
            result.setMsg(OptionDetails.LOGIN_OK.getMsg());
            result.setToken(JWTUtil.sign(user.toBuilder().build().toByteArray(), FinalData.LOGIN_EXPIRES));
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
        }
    }

    @Override
    public boolean checkUser(TestProto.User user) {
        return userMapper.findUserByTelAndPwd(user.getUserTel(), user.getUserPassword()) != null;
    }

    @Override
    public byte[] register(byte[] data) {
        TestProto.S2C_Register.Builder result = TestProto.S2C_Register.newBuilder();
        TestProto.C2S_Register builder = null;
        try {
            builder = TestProto.C2S_Register.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.REGISTER, OptionDetails.PROTOBUF_ERROR));

            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_REGISTER);
        }

        TestProto.User user = builder.getUser();
        TestProto.User old = userMapper.findUserByTel(user.getUserTel());
        if (old != null) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.REGISTER, OptionDetails.REGISTER_TEL_EXIST));

            result.setStatus(false);
            result.setMsg(OptionDetails.REGISTER_TEL_EXIST.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_REGISTER);
        }

        int ok = userMapper.insertUser(user);

        if (ok == 0) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.REGISTER, OptionDetails.SYSTEM_ERROR, user));

            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.REGISTER, OptionDetails.REGISTER_OK));

            result.setStatus(true);
            result.setMsg(OptionDetails.REGISTER_OK.getMsg());
        }

        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_REGISTER);
    }

    @Override
    public byte[] updatepwd(byte[] data) {
         TestProto.S2C_UpdatePwd.Builder result=TestProto.S2C_UpdatePwd.newBuilder();
         TestProto.C2S_UpdatePwd builder=null;
        try {
            builder = TestProto.C2S_UpdatePwd.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEPWD, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_REGISTER);
        }
        TestProto.User user = builder.getUser();
        String un= builder.getUser().getUserTel();
        String pwd=builder.getUser().getUserPassword();
        int ok=userMapper.updateUserPwd("pwd","un");
        if (ok == 0) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEPWD, OptionDetails.SYSTEM_ERROR, user));

            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEPWD, OptionDetails.UPDATE_OK));

            result.setStatus(true);
            result.setMsg(OptionDetails.UPDATE_OK.getMsg());
        }

        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEPWD);
    }

    @Override
    public byte[] updateEmail(byte[] data) {
        TestProto.S2C_UpdateEmail.Builder result=TestProto.S2C_UpdateEmail.newBuilder();
        TestProto.C2S_UpdateEmail builder=null;
        try {
            builder = TestProto.C2S_UpdateEmail.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEEMAIL, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEEMAIL);
        }
        TestProto.User user = builder.getUser();
        String un= builder.getUser().getUserEmail();
        String email=builder.getUser().getUserEmail();
        int ok=userMapper.updateUserEaili("email","un");
        if (ok == 0) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEEMAIL, OptionDetails.SYSTEM_ERROR, user));

            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEEMAIL, OptionDetails.UPDATEEMAIL_OK));

            result.setStatus(true);
            result.setMsg(OptionDetails.UPDATE_OK.getMsg());
        }

        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEEMAIL);
    }

    @Override
    public byte[] updateAll(byte[] data) {
        TestProto.S2C_UpdateAll.Builder result=TestProto.S2C_UpdateAll.newBuilder();
        TestProto.C2S_UpdateAll builder=null;
        try {
            builder = TestProto.C2S_UpdateAll.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEALL, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEALL);
        }

        TestProto.User user = builder.getUser();
        String utel=builder.getUser().getUserTel();
        TestProto.User old = userMapper.findUserByTel(user.getUserTel());
        if (old != null) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEALL, OptionDetails.REGISTER_TEL_EXIST));

            result.setStatus(false);
            result.setMsg(OptionDetails.REGISTER_TEL_EXIST.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEALL);
        }

        int ok = userMapper.updateAll(user,"utel");

        if (ok == 0) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEALL, OptionDetails.SYSTEM_ERROR, user));

            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATEALL, OptionDetails.REGISTER_OK));

            result.setStatus(true);
            result.setMsg(OptionDetails.UPDATEALL_OK.getMsg());
        }

        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEALL);


    }
}

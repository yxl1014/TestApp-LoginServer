package yxl.testapp.service.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pto.TestProto;
import yxl.testapp.logs.LogMsg;
import yxl.testapp.logs.LogUtil;
import yxl.testapp.logs.OptionDetails;
import yxl.testapp.mapper.UserMapper;
import yxl.testapp.service.UserService;
import yxl.testapp.util.*;

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

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JavaMailSender mailSender;

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
        //缓存


//        int type = builder.getLoginType();
        int type = 0;

        String un = builder.getUser().getUserTel();
        String pwd = builder.getUser().getUserPassword();
        TestProto.User user = null;
        switch (type) {
            case 0: {

                un = builder.getUser().getUserTel();
                //检查缓存是否存在
                boolean hasKey = redisUtil.findKey(un, pwd);
                if (hasKey == true) {
                    result.setMsg(OptionDetails.LOGIN_OK.getMsg());
                    byte[] bytes = result.buildPartial().toByteArray();
                    return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
                } else {
                    System.out.println("走数据库");
                    user = userMapper.findUserByTelAndPwd(un, pwd);
                    if (user == null) {
                        //日志
                        OptionDetails log = type == 0 ? OptionDetails.LOGIN_TEL_PWD_ERROR : OptionDetails.LOGIN_EMAIL_PWD_ERROR;
                        logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, log));
                        result.setStatus(false);
                        result.setMsg(log.getMsg());
                        byte[] bytes = result.buildPartial().toByteArray();
                        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
                    }
                    redisUtil.insertKey(un, pwd);
                }

                break;
            }
            case 1: {

                un = builder.getUser().getUserTel();
                //检查缓存是否存在
                boolean hasKey = redisUtil.findKey(un, pwd);
                if (hasKey == true) {
                    result.setMsg(OptionDetails.LOGIN_OK.getMsg());
                    byte[] bytes = result.buildPartial().toByteArray();
                    return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
                } else {
                    System.out.println("走数据库");
                    user = userMapper.findUserByTelAndPwd(un, pwd);
                    if (user == null) {
                        //日志
                        OptionDetails log = type == 0 ? OptionDetails.LOGIN_TEL_PWD_ERROR : OptionDetails.LOGIN_EMAIL_PWD_ERROR;
                        logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, log));
                        result.setStatus(false);
                        result.setMsg(log.getMsg());
                        byte[] bytes = result.buildPartial().toByteArray();
                        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
                    }
                    redisUtil.insertKey(un, pwd);
                }

                break;

            }
            default:
                logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, OptionDetails.PARAM_ERROR));
                result.setStatus(false);
                result.setMsg(OptionDetails.PARAM_ERROR.getMsg());
                byte[] bytes = result.buildPartial().toByteArray();
                return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
        }

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

    //修改密码

    @Override
    public byte[] updatePwdById(byte[] data) {
        TestProto.S2C_UpdatePwd.Builder result = TestProto.S2C_UpdatePwd.newBuilder();
        TestProto.C2S_UpdatePwd builder = null;
        try {
            builder = TestProto.C2S_UpdatePwd.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_PWD, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_REGISTER);
        }
        TestProto.User user = builder.getUser();
        int id = builder.getUser().getUserId();
        String pwd = builder.getUser().getUserPassword();
        TestProto.User ok = userMapper.findUserById(id);
        if (ok == null) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_PWD, OptionDetails.UPDATEPWD_ERROR_NOFOUNDID));

            result.setStatus(false);
            result.setMsg(OptionDetails.UPDATEPWD_ERROR_NOFOUNDID.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_REGISTER);
        }

        int flag = userMapper.updateUserPwdById(pwd, id);

        if (flag == 0) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_PWD, OptionDetails.SYSTEM_ERROR, user));

            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_PWD, OptionDetails.REGISTER_OK));

            result.setStatus(true);
            result.setMsg(OptionDetails.UPDATEPWD_OK.getMsg());
        }

        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEPWD);
    }

    //修改邮箱

    @Override
    public byte[] updateEmailById(byte[] data) {
        TestProto.S2C_UpdateEmail.Builder result = TestProto.S2C_UpdateEmail.newBuilder();
        TestProto.C2S_UpdateEmail builder = null;
        try {
            builder = TestProto.C2S_UpdateEmail.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_EMAIL, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEEMAIL);
        }
        TestProto.User user = builder.getUser();
        int id = builder.getUser().getUserId();
        String userEemail = builder.getUser().getUserEmail();
        TestProto.User ok = userMapper.findUserById(id);
        if (ok == null) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_EMAIL, OptionDetails.UPDATEEMAIL_ERROR_ID_NOTFOUND));

            result.setStatus(false);
            result.setMsg(OptionDetails.UPDATEPWD_ERROR_NOFOUNDID.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEEMAIL);
        }
        TestProto.User ok1 = userMapper.findUserByEmail(userEemail);
        if (ok1 != null) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_EMAIL, OptionDetails.UPDATEEMAIL_ERROR_EMAIL_EXIST));

            result.setStatus(false);
            result.setMsg(OptionDetails.UPDATEEMAIL_ERROR_ID_NOTFOUND.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEEMAIL);
        }
        int flag = userMapper.updateUserEmailById(userEemail, id);

        if (flag == 0) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_EMAIL, OptionDetails.SYSTEM_ERROR, user));

            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_EMAIL, OptionDetails.REGISTER_OK));

            result.setStatus(true);
            result.setMsg(OptionDetails.UPDATEEMAIL_OK.getMsg());
        }

        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEEMAIL);
    }

    //修改电话
    @Override
    public byte[] updateTelById(byte[] data) {
        TestProto.S2C_UpdateTel.Builder result = TestProto.S2C_UpdateTel.newBuilder();
        TestProto.C2S_UpdateTel builder = null;
        try {
            builder = TestProto.C2S_UpdateTel.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_TEL, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATETEL);
        }
        TestProto.User user = builder.getUser();
        int id = builder.getUser().getUserId();
        String userTel = builder.getUser().getUserTel();
        TestProto.User ok = userMapper.findUserById(id);
        if (ok != null) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_TEL, OptionDetails.UPDATEEMAIL_ERROR_ID_NOTFOUND));
            result.setStatus(false);
            result.setMsg(OptionDetails.UPDATEEMAIL_ERROR_ID_NOTFOUND.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATETEL);
        }
        TestProto.User ok1 = userMapper.findUserByTel(userTel);
        if (ok1 != null) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_TEL, OptionDetails.UPDATEEMAIL_ERROR_EMAIL_EXIST));

            result.setStatus(false);
            result.setMsg(OptionDetails.UPDATEEMAIL_ERROR_EMAIL_EXIST.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATETEL);
        }
        int flag = userMapper.updateUserTelById(id, userTel);

        if (flag == 0) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_TEL, OptionDetails.SYSTEM_ERROR, user));

            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_TEL, OptionDetails.REGISTER_OK));
            result.setStatus(true);
            result.setMsg(OptionDetails.UPDATEEMAIL_OK.getMsg());
        }

        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATETEL);
    }

    //修改全部信息
    @Override
    public byte[] updateAllById(byte[] data) {
        TestProto.S2C_UpdateAll.Builder result = TestProto.S2C_UpdateAll.newBuilder();
        TestProto.C2S_UpdateAll builder = null;
        try {
            builder = TestProto.C2S_UpdateAll.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_ALL, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEALL);
        }
        TestProto.User user = builder.getUser();
        TestProto.User.Builder old = userMapper.findUserById(user.getUserId()).toBuilder();

        if (!user.getUserName().isEmpty()) {
            old.setUserName(user.getUserName());
        }
        if (!user.getUserCompany().isEmpty()) {
            old.setUserCompany(user.getUserCompany());
        }
        if (!user.getUserHome().isEmpty()) {
            old.setUserHome(user.getUserHome());
        }
        if (!user.getUserPos().isEmpty()) {
            old.setUserPos(user.getUserPos());
        }
        if (!user.getUserIp().isEmpty()) {
            old.setUserIp(user.getUserIp());
        }


        int id = builder.getUser().getUserId();
        old.setUserId(id);
        TestProto.User ok = userMapper.findUserById(id);
        if (ok == null) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_ALL, OptionDetails.UPDATEALL_ERROR_ID_NOTFOUND));

            result.setStatus(false);
            result.setMsg(OptionDetails.UPDATEALL_ERROR_ID_NOTFOUND.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEALL);
        }


        int flag = userMapper.updateAllByID(old.build());
        System.out.println(flag);
        if (flag == 0) {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_ALL, OptionDetails.SYSTEM_ERROR, user));

            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {
            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_ALL, OptionDetails.REGISTER_OK));

            result.setStatus(true);
            result.setMsg(OptionDetails.UPDATEALL_OK.getMsg());
        }

        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEALL);


    }

    //发送邮箱验证码
    //TODO 这个方法不够解耦 他只需要给他个邮箱 然后发送验证码就够了 不用做其他的判断
    //TODO 绑定邮箱的流程 第一步先调用这个方法获取验证码 第二步调用下面那个方法验证验证码 验证通过了下一步才会调用updateEmail方法绑定
    //TODO 修改邮箱的流程 第一步先验证旧的邮箱，第二步验证新的邮箱，第三步再调用updateEmail方法绑定
    @Override
    public byte[] bindMailBox(byte[] data) {
        TestProto.S2C_BindMailBox.Builder result = TestProto.S2C_BindMailBox.newBuilder();
        TestProto.C2S_BindMailBox builder = null;
        BindMailBoxUtil bindMailBoxUtil = new BindMailBoxUtil();

        try {
            builder = TestProto.C2S_BindMailBox.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {

            logger.info(LogUtil.makeOptionDetails(LogMsg.BIND_MAILBOX, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_BINDMAILBOX);

        }
        String email = builder.getUser().getUserEmail();
        int uid = builder.getUser().getUserId();
        TestProto.User user = userMapper.findUserById(uid);
        if (user.getUserEmail() != null) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.BIND_MAILBOX, OptionDetails.BINDMAILBOX_ERROR_MAILBOX_EXIST));
            result.setStatus(false);
            result.setMsg(OptionDetails.BINDMAILBOX_ERROR_MAILBOX_EXIST.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_BINDMAILBOX);
        }


        String code = bindMailBoxUtil.randomCode();
        boolean flag = bindMailBoxUtil.sendEMail(email, code, mailSender, redisUtil);
        if (!flag) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.BIND_MAILBOX, OptionDetails.BINDMAILBOX_ERROR_CODE_FAIL, user));
            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        } else {

            //日志
            logger.info(LogUtil.makeOptionDetails(LogMsg.BIND_MAILBOX, OptionDetails.BINDMAILBOX_OK));
            result.setStatus(true);
            result.setMsg(OptionDetails.BINDMAILBOX_OK.getMsg());

        }
        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_BINDMAILBOX);
    }

    //验证邮箱
    @Override
    public byte[] checkMailBox(byte[] data) {
        TestProto.S2C_CheckMailBox.Builder result = TestProto.S2C_CheckMailBox.newBuilder();
        TestProto.C2S_CheckMailBox builder = null;
        BindMailBoxUtil bindMailBoxUtil = new BindMailBoxUtil();

        try {
            builder = TestProto.C2S_CheckMailBox.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.CHECK_MAILBOX, OptionDetails.PROTOBUF_ERROR));
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_CHECKMAILBOX);

        }
        TestProto.User user = builder.getUser();
        String code = builder.getCode();
        String userEmail = builder.getUser().getUserEmail();
        int id = builder.getUser().getUserId();

        boolean flag = bindMailBoxUtil.checkCode(userEmail, code , redisUtil);
        if (!flag) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.BIND_MAILBOX, OptionDetails.BINDMAILBOX_ERROR_CODE_FAIL, user));
            result.setStatus(false);
            result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
        }
        if (flag) {
            int flag1 = userMapper.insertUserEmailById(userEmail, id);
            if (flag1 == 0) {
                logger.info(LogUtil.makeOptionDetails(LogMsg.CHECK_MAILBOX, OptionDetails.CHECKEMAILBOX_ERROR, user));
                result.setStatus(false);
                result.setMsg(OptionDetails.SYSTEM_ERROR.getMsg());
            } else {
                logger.info(LogUtil.makeOptionDetails(LogMsg.CHECK_MAILBOX, OptionDetails.CHECKEMAILBOX_OK));
                result.setStatus(true);
                result.setMsg(OptionDetails.CHECKEMAILBOX_OK.getMsg());

            }
        }
        byte[] bytes = result.buildPartial().toByteArray();
        return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_CHECKMAILBOX);
    }
}

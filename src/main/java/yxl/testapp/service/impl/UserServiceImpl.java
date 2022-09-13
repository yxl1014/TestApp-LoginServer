package yxl.testapp.service.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yxl.testapp.domain.TestProto;
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
            logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, OptionDetails.LOGIN_OK));
            result.setStatus(true);
            result.setMsg(OptionDetails.LOGIN_OK.getMsg());
            result.setToken(JWTUtil.sign(user.toBuilder().build().toByteArray(), FinalData.LOGIN_EXPIRES));
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
        }
    }

}

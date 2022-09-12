package yxl.testapp.service.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yxl.testapp.domain.TestProto;
import yxl.testapp.logs.OptionDetails;
import yxl.testapp.mapper.UserMapper;
import yxl.testapp.service.UserService;
import yxl.testapp.util.ProtocolUtil;

/**
 * @author yxl
 * @date: 2022/9/12 下午2:50
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ProtocolUtil protocolUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public byte[] login(byte[] data) throws InvalidProtocolBufferException {
        TestProto.C2S_Login builder = TestProto.C2S_Login.parseFrom(data);

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
                TestProto.S2C_Login.Builder result = TestProto.S2C_Login.newBuilder();
                result.setStatus(false);
                result.setMsg(OptionDetails.PARAM_ERROR.getMsg());
                byte[] bytes = result.buildPartial().toByteArray();
                return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
        }


        return new byte[0];
    }

}

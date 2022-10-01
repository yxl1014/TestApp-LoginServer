package yxl.testapp.controller;


import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yxl.testapp.annotation.Check;
import pto.TestProto;
import yxl.testapp.logs.LogMsg;
import yxl.testapp.logs.LogUtil;
import yxl.testapp.logs.OptionDetails;
import yxl.testapp.service.UserService;
import yxl.testapp.util.ProtocolUtil;

/**
 * @author yxl
 * @date: 2022/8/30 上午11:56
 */

@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LogUtil.getLogger(UserController.class);

    @Autowired
    private ProtocolUtil protocolUtil;

    @Autowired
    private UserService userService;


    /**
     * 登录
     */
    @PostMapping("/login")
    public byte[] login(@RequestBody byte[] data) {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, OptionDetails.PROTOBUF_ERROR));
            TestProto.S2C_Login.Builder result = TestProto.S2C_Login.newBuilder();
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_LOGIN);
        }
        return userService.login(temp);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public byte[] register(@RequestBody byte[] data) {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.REGISTER, OptionDetails.PROTOBUF_ERROR));
            TestProto.S2C_Register.Builder result = TestProto.S2C_Register.newBuilder();
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_REGISTER);
        }
        return userService.register(temp);
    }

    /**
     * 修改密码
     */

    @PostMapping("/updatePwdById")
    public byte[] updatePwdByTel(@RequestBody byte[] data) {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_PWD, OptionDetails.PROTOBUF_ERROR));
            TestProto.S2C_UpdatePwd.Builder result = TestProto.S2C_UpdatePwd.newBuilder();
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEPWD);
        }
        return userService.updatePwdById(temp);
    }


    /**
     * 修改邮箱
     */
    @PostMapping("/updateEmailById")

    public byte[] updatEemailByTel(@RequestBody byte[] data) {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_EMAIL, OptionDetails.PROTOBUF_ERROR));
            TestProto.S2C_UpdatePwd.Builder result = TestProto.S2C_UpdatePwd.newBuilder();
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEEMAIL);
        }
        return userService.updateEmailById(temp);
    }

    /**
     * 修改电话
     */
    @PostMapping("/updateTelById")
    public byte[] updateTelByEml(@RequestBody byte[] data) {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_TEL, OptionDetails.PROTOBUF_ERROR));
            TestProto.S2C_UpdateTel.Builder result = TestProto.S2C_UpdateTel.newBuilder();
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATETEL);
        }
        return userService.updateTelById(temp);
    }

    /**
     * 修改全部信息
     */
    @PostMapping("/updateAllById")
    public byte[] updateAllByTel(@RequestBody byte[] data) {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.UPDATE_ALL, OptionDetails.PROTOBUF_ERROR));
            TestProto.S2C_UpdatePwd.Builder result = TestProto.S2C_UpdatePwd.newBuilder();
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOBUF_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_UPDATEALL);
        }
        return userService.updateAllById(temp);
    }


    @PostMapping("/bindMailbox")
    public byte[] bindMailox(@RequestBody byte[] data) {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info((LogUtil.makeOptionDetails(LogMsg.BIND_MAILBOX, OptionDetails.PROTOBUF_ERROR)));
            TestProto.S2C_BindMailBox.Builder result = TestProto.S2C_BindMailBox.newBuilder();
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOCOL_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_BINDMAILBOX);
        }

        return userService.bindMailBox(temp);
    }

    @PostMapping("/checkMailbox")
    public byte[] checkMailox(@RequestBody byte[] data) {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info((LogUtil.makeOptionDetails(LogMsg.BIND_MAILBOX, OptionDetails.PROTOBUF_ERROR)));
            TestProto.S2C_CheckMailBox.Builder result = TestProto.S2C_CheckMailBox.newBuilder();
            result.setStatus(false);
            result.setMsg(OptionDetails.PROTOCOL_ERROR.getMsg());
            byte[] bytes = result.buildPartial().toByteArray();
            return protocolUtil.encodeProtocol(bytes, bytes.length, TestProto.Types.S2C_BINDMAILBOX);
        }
        return userService.checkMailBox(temp);
    }


    /**
     * 无check测试
     */
    @GetMapping("/")
    @ResponseBody
    public String test() {
        logger.info(LogUtil.makeOptionDetails(LogMsg.TEST, OptionDetails.TEST_OK));
        return "hello";
    }


    /**
     * 有check测试
     */
    @GetMapping("/check")
    @ResponseBody
    @Check
    public String test1() {
        return "hello";
    }

}

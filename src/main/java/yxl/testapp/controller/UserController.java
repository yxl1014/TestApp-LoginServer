package yxl.testapp.controller;


import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yxl.testapp.domain.TestProto;
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


    @PostMapping("/login")
    public byte[] login(@RequestBody byte[] data){
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

    @GetMapping("/")
    @ResponseBody
    public String test() {
        logger.info(LogUtil.makeOptionDetails(LogMsg.TEST, OptionDetails.TEST_OK));
        return "hello";
    }

}

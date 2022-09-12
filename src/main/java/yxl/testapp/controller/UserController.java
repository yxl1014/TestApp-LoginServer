package yxl.testapp.controller;


import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public byte[] login(@RequestBody byte[] data) throws InvalidProtocolBufferException {
        byte[] temp = protocolUtil.decodeProtocol(data);
        if (temp == null) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.LOGIN, OptionDetails.PROTOCOL_ERROR));
            //TODO 添加protobuf
            return new byte[10];
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

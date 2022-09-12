package yxl.testapp.logs;

/**
 * @author yxl
 * @date: 2022/9/12 下午1:10
 */
public enum OptionDetails {

    //测试
    TEST_OK("测试成功","无敌"),

    //自定义协议
    PROTOCOL_ERROR("协议错误","协议错误"),


    //登录
    LOGIN_OK("登录成功", "登录成功"), LOGIN_FINAL_EXIST("登陆失败","帐号已存在"),
    LOGIN_PWD_ERROR("登陆失败","密码错误")
    ;


    private String status;
    private String msg;

    OptionDetails(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

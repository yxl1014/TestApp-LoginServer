package yxl.testapp.logs;

/**
 * @author yxl
 * @date: 2022/9/12 下午1:10
 */
public enum OptionDetails {

    //测试
    TEST_OK("测试成功", "无敌"),

    //自定义协议
    PROTOCOL_ERROR("协议错误", "协议错误"), PROTOBUF_ERROR("PROTO数据错误", "PROTO数据错误"),


    //系统
    PARAM_ERROR("参数异常", "参数异常"), SYSTEM_ERROR("系统错误", "系统错误"),


    //拦截器
    NO_CONTROLLER("拦截器错误", "没有此接口"), NO_TOKEN("拦截器错误", "没有携带token"),
    TOKEN_ERROR("拦截器错误", "token无效"), TOKEN_EXPIRES("拦截器错误", "token已过期"),


    NO_CHECK("拦截器日志", "访问无CHECK接口"), CHECK("拦截器日志", "访问有CHECK接口"),
    CHECK_OK("拦截器日志", "token验证成功"),


    //登录
    LOGIN_OK("登录成功", "登录成功"), UPDATE_IP("登录成功", "Ip地址发生变化"),
    LOGIN_TEL_PWD_ERROR("登陆失败", "电话或密码错误"), LOGIN_EMAIL_PWD_ERROR("登陆失败", "邮箱或密码错误"),


    //注册
    REGISTER_TEL_EXIST("注册失败", "此电话已存在"), REGISTER_OK("注册成功", "注册成功"),


    //修改密码
    UPDATEPWD_OK("修改成功", "修改成功"), UPDATEPWD_ERROR_NOFOUNDID("修改失败", "未找到用户id"),
    UPDATEPWD_ERRORONE_TEL_NOTFOUND("修改失败", "未找到该电话"),

    //修改邮箱
    UPDATEEMAIL_OK("修改成功", "修改成功"), UPDATEEMAIL_ERROR_ID_NOTFOUND("修改失败", "未找到用户id"),
    UPDATEEMAIL_ERROR_EMAIL_EXIST("修改失败", "此邮箱已注册"),

    //修改全部
    UPDATEALL_OK("修改成功", "修改成功"), UPDATEALL_ERROR_ID_NOTFOUND("修改失败", "未找到此电话，无法修改"),

    //修改电话
    UPDATETEL_OK("修改成功", "修改成功"), UPDATETEL_ERROR_TEL_EXIST("修改失败", "此电话已注册"),
    UPDATE_ERROR_ID_NOTFOUND("修改失败", "未找到此邮箱"),


    //绑定邮箱
    BINDMAILBOX_OK("邮箱绑定成功", "验证码发送成功"), BINDMAILBOX_ERROR_MAILBOX_EXIST("邮箱绑定失败", "此邮箱已被绑定"),
    BINDMAILBOX_ERROR_CODE_FAIL("邮箱绑定失败", "验证码未发送"),

    //验证邮箱
    CHECKEMAILBOX_OK("验证邮箱成功", "验证成功"), CHECKEMAILBOX_ERROR("验证邮箱失败", "验证码不正确");
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

    public String getAll() {
        return this.status + "---" + this.msg;
    }
}

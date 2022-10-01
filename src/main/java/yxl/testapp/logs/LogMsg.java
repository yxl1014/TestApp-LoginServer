package yxl.testapp.logs;

/**
 * @author yxl
 * @date: 2022/9/12 下午12:47
 */

public enum LogMsg {
    //测试
    TEST("测试一下"),
    LOGIN("登录操作"), REGISTER("注册操作"), INTERCEPTOR("拦截器"), UPDATE_PWD("修改密码"),
    UPDATE_EMAIL("修改邮箱"), UPDATE_ALL("修改全部"), UPDATE_TEL("修改电话"), BIND_MAILBOX("绑定邮箱"),
    CHECK_MAILBOX("验证邮箱");


    private String name;

    LogMsg(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

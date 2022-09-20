package yxl.testapp.logs;

/**
 * @author yxl
 * @date: 2022/9/12 下午12:47
 */

public enum LogMsg {
    //测试
    TEST("测试一下"),
    LOGIN("登录操作"), REGISTER("注册操作"),INTERCEPTOR("拦截器"),UPDATEPWD("修改密码"),
    UPDATEEMAIL("修改邮箱"),UPDATEALL("修改全部"),UPDATETEL("修改电话")
    ;


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

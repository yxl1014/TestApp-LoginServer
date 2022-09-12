package yxl.testapp.mapper;

import org.apache.ibatis.annotations.*;
import yxl.testapp.domain.TestProto;

/**
 * @author yxl
 * @date: 2022/9/12 下午3:54
 */


@Mapper
public interface UserMapper {

    /**
     * 通过电话和密码查询用户
     * */
    @Results({
            @Result(property = "userId_", column = "userId"),
            @Result(property = "userName_", column = "userName"),
            @Result(property = "userTel_", column = "userTel"),
            @Result(property = "userEmail_", column = "userEmail"),
            @Result(property = "userPassword_", column = "userPassword"),
            @Result(property = "userType_", column = "userType"),
            @Result(property = "userMoney_", column = "userMoney"),
            @Result(property = "userCompany_", column = "userCompany"),
            @Result(property = "userHome_", column = "userHome"),
            @Result(property = "userIp_", column = "userIp"),
            @Result(property = "userPos_", column = "userPos")
    })
    @Select("select userId,userName,userTel,userEmail,userPassword,userType,userMoney,userCompany,userHome,userIp,userPos" +
            " from Users " +
            "where userTel = #{un} and userPassword = #{pwd}")
    TestProto.User findUserByTelAndPwd(@Param("un")String un,@Param("pwd") String pwd);

    /**
     * 通过电话和密码查询用户
     * */
    @Results({
            @Result(property = "userId_", column = "userId"),
            @Result(property = "userName_", column = "userName"),
            @Result(property = "userTel_", column = "userTel"),
            @Result(property = "userEmail_", column = "userEmail"),
            @Result(property = "userPassword_", column = "userPassword"),
            @Result(property = "userType_", column = "userType"),
            @Result(property = "userMoney_", column = "userMoney"),
            @Result(property = "userCompany_", column = "userCompany"),
            @Result(property = "userHome_", column = "userHome"),
            @Result(property = "userIp_", column = "userIp"),
            @Result(property = "userPos_", column = "userPos")
    })
    @Select("select userId,userName,userTel,userEmail,userPassword,userType,userMoney,userCompany,userHome,userIp,userPos" +
            " from Users " +
            "where userEmail = #{un} and userPassword = #{pwd}")
    TestProto.User findUserByEmailAndPwd(@Param("un")String un,@Param("pwd") String pwd);
}

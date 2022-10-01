package yxl.testapp.mapper;

import org.apache.ibatis.annotations.*;
import pto.TestProto;

/**
 * @author yxl
 * @date: 2022/9/12 下午3:54
 */


@Mapper
public interface UserMapper {

    /**
     * 通过电话和密码查询用户
     */
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
    TestProto.User findUserByTelAndPwd(@Param("un") String un, @Param("pwd") String pwd);

    /**
     * 通过邮箱和密码查询用户
     */
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
    TestProto.User findUserByEmailAndPwd(@Param("un") String un, @Param("pwd") String pwd);


    /**
     * 通过电话查询用户
     */
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
            "where userTel = #{tel}")
    TestProto.User findUserByTel(@Param("tel")String userTel);

    /**
     * 通过ID查询用户
     */
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
            "where userId = #{id}")
    TestProto.User findUserById(@Param("id") int id);
    /**
     * 添加用户
     */
    @Insert("insert into Users(userName,userTel,userPassword,userIp,userPos) " +
            "values(#{userName_},#{userTel_},#{userPassword_},#{userIp_},#{userPos_})")
    int insertUser(TestProto.User user);

    @Update("update Users set userIp = #{userIp} where userId = #{userId}")
    void updateUserIPByUserId(@Param("userIp") String userIp, @Param("userId") int userId);

    @Update("update Users set userIp = #{userPos} where userId = #{userId}")
    void updateUserPosByUserId(@Param("userPos") String userPos, @Param("userId") int userId);

    /**
     * 修改密码
     */
    
    @Update("update Users set userPassword =#{pwd} where userId=#{id}")
    int updateUserPwdById(@Param("pwd") String pwd, @Param("id") int id);

    /**
     * 修改邮箱
     */
    
    @Update("update Users set userEmail =#{userEmail} where userId=#{id}")
    int  updateUserEmailById(@Param("userEmail") String userEmail, @Param("id") int id);

    @Update("update Users set userTel =#{userTel} where userId=#{id}")
    int updateUserTelById(@Param("id") int id, @Param("userTel") String userTel);

    @Select("select userEmail from Users where userEmail = #{userEmail}")
    TestProto.User findUserByEmail(@Param("userEmail")String userEmail);


    @Update("update Users set userName=#{userName_},userIp=#{userIp_},userPos=#{userPos_}," +
            "userCompany=#{userCompany_},userHome=#{userHome_} " +
            "where userId=#{userId_}")
    int updateAllByID(TestProto.User user);


    @Update("update Users set userEmail =#{email} where userId=#{id}")
    int insertUserEmailById(@Param("email") String email,@Param("id") int id);

}

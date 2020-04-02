package com.testdb.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.testdb.demo.entity.Role;
import com.testdb.demo.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

//    @Insert("INSERT INTO USER(NAME, AGE, ADDRESS) VALUES(#{name}, #{age}, #{address})")
//    int insert(@Param("name") String name, @Param("age") Integer age, @Param("address")String address);

    @Select("SELECT * FROM USER WHERE USERNAME=#{username}")
    User selectByUsername(@Param("username") String username);

    @Insert("INSERT INTO USER_ROLE(USER_ID, ROLE_ID) VALUES(#{user_id}, #{role_id})")
    Boolean createUser(@Param("user_id") String user_id, @Param("role_id") String role_id);

    @Select("SELECT ID FROM USER WHERE USERNAME=#{username}")
    int selectIdByUsername(@Param("username") String username);

    User selectUserAndRoles(@Param("username") String username);

    void updateUserInfo(User user);
}

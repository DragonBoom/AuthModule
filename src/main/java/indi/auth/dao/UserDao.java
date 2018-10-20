package indi.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import indi.auth.entity.UserDO;

public interface UserDao {

    @Select("select id from user where id = #{id}")
    UserDO getById(Long id);

    @Select("select id from user where username = #{username}")
    UserDO getByUsername(String username);

    @Insert("insert into user (id, username, password, avatar) values (#{id}, #{username}, #{password}, #{avatar})")
    void add(UserDO user);

    @Select("select * from user limit #{index}, #{offset}")
    List<UserDO> query(@Param("index") int index, @Param("offset") int offset);
    
    @Select("select count(1) from user")
    Integer getCount();
}

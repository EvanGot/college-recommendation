package com.wzh.dao;

import com.wzh.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(@Param("username")String username);

    User getByUsername(@Param("username")String username,@Param("type")String type);

    List<User> findAll(User user);
}
package com.wzh.dao;

import com.wzh.model.School;

import java.util.List;

public interface SchoolMapper {
    int deleteByPrimaryKey(String id);

    int insert(School record);

    int insertSelective(School record);

    School selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(School record);

    int updateByPrimaryKey(School record);

    List<School> findAll(School record);

}
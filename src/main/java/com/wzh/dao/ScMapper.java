package com.wzh.dao;

import com.wzh.model.Sc;

import java.util.List;

public interface ScMapper {
    int deleteByPrimaryKey(String id);

    int insert(Sc record);

    int insertSelective(Sc record);

    Sc selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Sc record);

    int updateByPrimaryKey(Sc record);

    Sc check(Sc record);

    List<Sc> findAll(Sc sc);
}

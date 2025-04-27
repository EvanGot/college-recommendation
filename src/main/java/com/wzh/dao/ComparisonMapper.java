package com.wzh.dao;

import com.wzh.model.Comparison;
import com.wzh.model.Sc;

import java.util.List;

public interface ComparisonMapper {
    int deleteByPrimaryKey(String id);

    int insert(Comparison record);

    int insertSelective(Comparison record);

    Comparison selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Comparison record);

    int updateByPrimaryKey(Comparison record);

    Comparison check(Comparison record);

    List<Comparison> findAll(Comparison record);
}
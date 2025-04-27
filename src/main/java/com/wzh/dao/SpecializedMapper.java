package com.wzh.dao;

import com.wzh.model.Specialized;

import java.util.List;

public interface SpecializedMapper {
    int deleteByPrimaryKey(String id);

    int insert(Specialized record);

    int insertSelective(Specialized record);

    Specialized selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Specialized record);

    int updateByPrimaryKey(Specialized record);

    List<Specialized> findAll(Specialized specialized);

    List<Specialized> findrecom(Specialized specialized);

    List<Specialized> findgroup(Specialized specialized);


    List<Specialized> findeact(Specialized specialized);

}
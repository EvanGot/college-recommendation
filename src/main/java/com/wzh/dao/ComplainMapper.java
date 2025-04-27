package com.wzh.dao;

import com.wzh.model.Complain;
import com.wzh.model.News;

import java.util.List;

public interface ComplainMapper {
    int deleteByPrimaryKey(String id);

    int insert(Complain record);

    int insertSelective(Complain record);

    Complain selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Complain record);

    int updateByPrimaryKey(Complain record);

    List<Complain> findAll(Complain record);

}
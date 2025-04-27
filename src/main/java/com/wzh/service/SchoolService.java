package com.wzh.service;

import com.wzh.dao.SchoolMapper;
import com.wzh.model.School;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SchoolService {
    @Resource
    SchoolMapper schoolMapper;
    public int save(School school) {
        return schoolMapper.insertSelective(school);
    }
    //    删除数据
    public int del(String id) {
        return schoolMapper.deleteByPrimaryKey(id);
    }
    //    获取数据
    public School get(String id) {
        return schoolMapper.selectByPrimaryKey(id);
    }
    //    更新数据
    public int  update( School u) {
        return schoolMapper.updateByPrimaryKeySelective(u);
    }

    public List<School> findAll(School school) {
        return schoolMapper.findAll(school);
    }
}

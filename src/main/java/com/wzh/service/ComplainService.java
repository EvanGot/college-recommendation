package com.wzh.service;

import com.wzh.dao.ComplainMapper;
import com.wzh.model.Complain;
import com.wzh.model.News;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ComplainService {
    @Resource
    ComplainMapper complainMapper;
    public int save(Complain complain) {
        return complainMapper.insertSelective(complain);
    }
    //    删除数据
    public int del(String id) {
        return complainMapper.deleteByPrimaryKey(id);
    }
    //    获取数据
    public Complain get(String id) {
        return complainMapper.selectByPrimaryKey(id);
    }
    //    更新数据
    public int  update( Complain u) {
        return complainMapper.updateByPrimaryKeySelective(u);
    }

    public List<Complain> findAll(Complain complain) {
        return complainMapper.findAll(complain);
    }
}

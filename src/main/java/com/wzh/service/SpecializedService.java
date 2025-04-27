package com.wzh.service;

import com.wzh.dao.SpecializedMapper;
import com.wzh.model.Specialized;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SpecializedService {
    @Resource
    SpecializedMapper specializedMapper;
    public int save( Specialized specialized) {
        return specializedMapper.insertSelective(specialized);
    }
    //    删除数据
    public int del(String id) {
        return specializedMapper.deleteByPrimaryKey(id);
    }
    //    获取数据
    public Specialized get(String id) {
        return specializedMapper.selectByPrimaryKey(id);
    }
    //    更新数据
    public int  update( Specialized u) {
        return specializedMapper.updateByPrimaryKeySelective(u);
    }

    public List<Specialized> findAll(Specialized t) {
        return specializedMapper.findAll(t);
    }


    public List<Specialized> findgroup(Specialized t) {
        return specializedMapper.findgroup(t);
    }
    public List<Specialized> findrecom(Specialized t) {
        return specializedMapper.findrecom(t);
    }

    public List<Specialized> findeact(Specialized t) {
        return specializedMapper.findeact(t);
    }



}

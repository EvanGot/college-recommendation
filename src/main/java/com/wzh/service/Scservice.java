package com.wzh.service;

import com.wzh.dao.ScMapper;
import com.wzh.model.Sc;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class Scservice {
    @Resource
    ScMapper scMapper;
    //    判断是否收藏
    public Sc check(Sc sc){
        return scMapper.check(sc);
    }
    //    取消收藏
    public int del(String id) {
        return scMapper.deleteByPrimaryKey(id);
    }

    //    保存收藏
    public int add(Sc sc) {
        return scMapper.insertSelective(sc);
    }

    public List<Sc> findAll(Sc sc) {
        return scMapper.findAll(sc);
    }


}

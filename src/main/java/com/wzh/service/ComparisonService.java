package com.wzh.service;

import com.wzh.dao.ComparisonMapper;
import com.wzh.model.Comparison;
import com.wzh.model.Sc;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ComparisonService {
    @Resource
    ComparisonMapper comparisonMapper;
    //    判断是否对比
    public Comparison check(Comparison comparison){
        return comparisonMapper.check(comparison);
    }
    //    取消收藏
    public int del(String id) {
        return comparisonMapper.deleteByPrimaryKey(id);
    }

    //    保存收藏
    public int add(Comparison comparison) {
        return comparisonMapper.insertSelective(comparison);
    }

    public List<Comparison> findAll(Comparison comparison) {
        return comparisonMapper.findAll(comparison);
    }
}

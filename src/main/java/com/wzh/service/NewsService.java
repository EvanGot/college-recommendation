package com.wzh.service;

import com.wzh.dao.NewsMapper;
import com.wzh.model.News;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewsService {
    @Resource
    NewsMapper newsMapper;
    public int save(News news) {
        return newsMapper.insertSelective(news);
    }
    //    删除数据
    public int del(String id) {
        return newsMapper.deleteByPrimaryKey(id);
    }
    //    获取数据
    public News get(String id) {
        return newsMapper.selectByPrimaryKey(id);
    }
    //    更新数据
    public int  update( News u) {
        return newsMapper.updateByPrimaryKeySelective(u);
    }

    public List<News> findAll(News news) {
        return newsMapper.findAll(news);
    }
}

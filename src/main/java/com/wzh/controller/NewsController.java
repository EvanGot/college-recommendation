package com.wzh.controller;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzh.model.News;
import com.wzh.service.NewsService;
import com.wzh.until.JsonObject;
import com.wzh.until.PageRet;
import com.wzh.until.Time;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Resource
    NewsService newsService;
    @Value("${server.port}")
    private String dkh;


    //    分页查询
    @RequestMapping(value = "/pagegetall")
    @ResponseBody
    public PageRet list(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "limit", defaultValue = "2") int limit,
                        News news){
        Page<News> pageInfo = PageHelper.startPage(page, limit, "time desc");
        List<News> list = newsService.findAll(news);
        PageInfo<News> all = new PageInfo<>(list);
        return new PageRet(0, "", all.getList(), (int) all.getTotal());
    }

    //新增数据
    @RequestMapping("/add")
    @ResponseBody
    public JsonObject save(News news) {
        try {
            news.setId(IdUtil.simpleUUID());
            news.setTime(Time.getTime());
            newsService.save(news);
            System.out.println("添加数据");
            return new JsonObject("200", "", null);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }
    @RequestMapping("/del")
    @ResponseBody
    public JsonObject del(@RequestParam("id") String ids[]){
        try {
            for (String id : ids) {
                newsService .del(id);
            }
            return new JsonObject("200", "", null);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }
    @RequestMapping("/get")
    @ResponseBody
    public JsonObject get(@RequestParam("id") String id) {
        try {
            News u = newsService.get(id);
            return new JsonObject("200", "", u);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }
    //    更新
    @RequestMapping("/update")
    @ResponseBody
    public JsonObject update(News news) {
        try {
            newsService.update(news);
            System.out.println("更新数据");
            return new JsonObject("200", "", null);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }

    @RequestMapping("findAll")
    @ResponseBody
    public JsonObject findAll(News news){
        try {

            List<News> list = newsService.findAll(news);

            return new JsonObject("200","", list);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }
    }

}

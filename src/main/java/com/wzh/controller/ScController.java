package com.wzh.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzh.model.*;
import com.wzh.service.NewsService;
import com.wzh.service.SchoolService;
import com.wzh.service.Scservice;
import com.wzh.service.SpecializedService;
import com.wzh.until.JsonObject;
import com.wzh.until.PageRet;
import com.wzh.until.Time;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sc")
public class ScController {
    @Resource
    Scservice scservice;
    @RequestMapping("/check")
    @ResponseBody
    public JsonObject check(Sc sc){
        Sc s = scservice.check(sc);
        System.out.println(s);
        if (s !=null){
            return new JsonObject("200", "", "");
        }
        else {
            return new JsonObject("500", "失败", "");
        }
    }
    @RequestMapping("/qx")
    @ResponseBody
    public JsonObject del(Sc sc){
        Sc s = scservice.check(sc);
        if (s !=null)
            scservice.del(s.getId());
        return new JsonObject("200", "", "");
    }
    @Resource
    SpecializedService specializedService;
    @Resource
    SchoolService schoolService;

    @RequestMapping("add")
    @ResponseBody
    public JsonObject msave(Sc sc){
        try{
            Sc s = scservice.check(sc);
            if (s ==null){
                Specialized specialized = specializedService.get(sc.getTid());
                if (specialized!=null){
                    sc.setName(specialized.getName());
                    sc.setScore(specialized.getScore());
                    School school = schoolService.get(specialized.getTid());
                    if (school!=null){
                        sc.setSchool(school.getName());
                    }
                }
                sc.setId(UUID.randomUUID().toString().replace("-", ""));
                sc.setTime(Time.getTime());
                scservice.add(sc);
                System.out.println(sc);
                return new JsonObject("200","", "");
            }
            else {
                return new JsonObject("500", "失败", "");
            }

        }
        catch (Exception e){
            System.out.println(e.toString());
            return new JsonObject("500","失败","");
        }
    }

    @Resource
    NewsService newsService;
    @RequestMapping("/getlist")
    @ResponseBody
    public JsonObject getlist(Sc sc, @RequestParam("url") String url) {
        try{
            List<Sc> list = scservice.findAll(sc);
            List<News> all_list = new ArrayList<>();
            for (int i = 0; i < list.size(); i++){
                News news = newsService.get(list.get(i).getTid());
                if (news !=null){
                    if (news.getContent()==null||news.getContent().equals("")){
                        all_list.add(news);
                    }
                    else {
                        if (news.getContent().contains("http://localhost:8088/")){
                            String test01 = news.getContent();
                            test01 = test01.replace("http://localhost:8088/", url);
                            news.setContent(test01);
                            all_list.add(news);
                        }
                        else {
                            all_list.add(news);

                        }
                    }
                }

            }
            return new JsonObject("200","", JSON.toJSONString(all_list));
        }
        catch (Exception e){
            System.out.println(e.toString());
            return new JsonObject("500","失败","");
        }
    }
    @RequestMapping("/del")
    @ResponseBody
    public JsonObject del(@RequestParam("id") String ids[]){
        try {
            for (String id : ids) {
                scservice .del(id);
            }
            return new JsonObject("200", "", null);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }
    @RequestMapping(value = "/pagegetall")
    @ResponseBody
    public PageRet list(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "limit", defaultValue = "2") int limit,
                        Sc sc){
        Page<Sc> pageInfo = PageHelper.startPage(page, limit, "time desc");
        List<Sc> list = scservice.findAll(sc);
        List<Sc> scList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Sc sc1 = list.get(i);
            Specialized specialized  = specializedService.get(list.get(i).getTid());
            if (specialized!=null){
                sc1.setZname(specialized.getName());
                sc1.setScore(specialized.getScore());
                sc1.setYear(specialized.getYear());
                School school  = schoolService.get(specialized.getTid());
                if (school!=null){
                    sc1.setSname(school.getName());
                    sc1.setAddress(school.getAddress());
                    sc1.setSctype(school.getSctype());
                    sc1.setType(school.getType());
                    sc1.setZytype(school.getZytype());
                    scList.add(sc1);
                }
            }


        }
        PageInfo<Sc> all = new PageInfo<>(list);
        return new PageRet(0, "", all.getList(), (int) all.getTotal());
    }

}

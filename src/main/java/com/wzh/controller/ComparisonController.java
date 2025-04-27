package com.wzh.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzh.model.*;
import com.wzh.service.*;
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
@RequestMapping("/comparison")
public class ComparisonController {
    @Resource
    ComparisonService comparisonService;
    @RequestMapping("/check")
    @ResponseBody
    public JsonObject check(Comparison comparison){
        Comparison s = comparisonService.check(comparison);
        System.out.println(s);
        if (s !=null){
            return new JsonObject("200", "", "");
        }
        else {
            return new JsonObject("500", "失败", "");
        }
    }



    @RequestMapping("add")
    @ResponseBody
    public JsonObject msave(Comparison comparison){
        try{
            Comparison s = comparisonService.check(comparison);
            if (s ==null){
                comparison.setId(UUID.randomUUID().toString().replace("-", ""));
                comparison.setTime(Time.getTime());
                comparisonService.add(comparison);
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
    SchoolService schoolService;
    @Resource
    SpecializedService specializedService;




    @RequestMapping(value = "/pagegetall")
    @ResponseBody
    public PageRet list(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "limit", defaultValue = "2") int limit,
                        Comparison comparison){
        Page<Comparison> pageInfo = PageHelper.startPage(page, limit, "time desc");
        List<Comparison> list = comparisonService.findAll(comparison);
        List<Comparison> comparisonList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Comparison comparison1 = list.get(i);
            Specialized specialized  = specializedService.get(list.get(i).getTid());
            if (specialized!=null){
                comparison1.setZname(specialized.getName());
                comparison1.setScore(specialized.getScore());
                comparison1.setYear(specialized.getYear());
                School school  = schoolService.get(specialized.getTid());
                if (school!=null){
                    comparison1.setSname(school.getName());
                    comparison1.setAddress(school.getAddress());
                    comparison1.setSctype(school.getSctype());
                    comparison1.setType(school.getType());
                    comparison1.setZytype(school.getZytype());
                    comparisonList.add(comparison1);
                }
            }


        }

        PageInfo<Comparison> all = new PageInfo<>(comparisonList);
        return new PageRet(0, "", all.getList(), (int) all.getTotal());
    }
    @RequestMapping("/del")
    @ResponseBody
    public JsonObject del(@RequestParam("id") String ids[]){
        try {
            for (String id : ids) {
                comparisonService .del(id);
            }
            return new JsonObject("200", "", null);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }
    @RequestMapping("/qx")
    @ResponseBody
    public JsonObject del(Comparison comparison){
        System.err.println("123");
        Comparison s = comparisonService.check(comparison);
        if (s !=null)
            comparisonService.del(s.getId());
        return new JsonObject("200", "", "");
    }
}

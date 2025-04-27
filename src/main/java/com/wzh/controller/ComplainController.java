package com.wzh.controller;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzh.model.Complain;
import com.wzh.model.News;
import com.wzh.service.ComplainService;
import com.wzh.until.JsonObject;
import com.wzh.until.PageRet;
import com.wzh.until.Time;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/complain")
public class ComplainController {
    @Resource
    ComplainService complainService;
    //    分页查询
    @RequestMapping(value = "/pagegetall")
    @ResponseBody
    public PageRet list(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "limit", defaultValue = "2") int limit,
                        Complain complain){
        Page<Complain> pageInfo = PageHelper.startPage(page, limit, "time desc");
        List<Complain> list = complainService.findAll(complain);
        PageInfo<Complain> all = new PageInfo<>(list);
        return new PageRet(0, "", all.getList(), (int) all.getTotal());
    }

    //新增数据
    @RequestMapping("/add")
    @ResponseBody
    public JsonObject save(Complain complain) {
        try {
            complain.setId(IdUtil.simpleUUID());
            complain.setTime(Time.getTime());
            complainService.save(complain);
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
                complainService .del(id);
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
            Complain u = complainService.get(id);
            return new JsonObject("200", "", u);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }
    //    更新
    @RequestMapping("/update")
    @ResponseBody
    public JsonObject update(Complain complain) {
        try {
            complainService.update(complain);
            System.out.println("更新数据");
            return new JsonObject("200", "", null);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }

    @RequestMapping("findAll")
    @ResponseBody
    public JsonObject findAll(Complain complain){
        try {
            List<Complain> list = complainService.findAll(complain);
            return new JsonObject("200","", list);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }
    }
}

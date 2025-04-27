package com.wzh.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzh.model.Comparison;
import com.wzh.model.Sc;
import com.wzh.model.School;
import com.wzh.model.Specialized;
import com.wzh.service.ComparisonService;
import com.wzh.service.SchoolService;
import com.wzh.service.Scservice;
import com.wzh.service.SpecializedService;
import com.wzh.until.JsonObject;
import com.wzh.until.PageRet;
import com.wzh.until.Time;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/specialized")
public class SpecializedController {
    @Resource
    SpecializedService specializedService;
    @Resource
    ComparisonService comparisonService;
    @Value("${server.port}")
    private String dkh;


    //    分页查询
    @RequestMapping(value = "/pagegetall")
    @ResponseBody
    public PageRet list(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "limit", defaultValue = "2") int limit,
                        Specialized specialized){
        Page<Specialized> pageInfo = PageHelper.startPage(page, limit, "time desc");
        List<Specialized> list = specializedService.findAll(specialized);
        PageInfo<Specialized> all = new PageInfo<>(list);
        return new PageRet(0, "", all.getList(), (int) all.getTotal());
    }
    @Resource
    Scservice scservice;
    @RequestMapping(value = "/pagegetallsee")
    @ResponseBody
    public PageRet pagegetallsee(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "limit", defaultValue = "2") int limit,
                        @RequestParam("uid") String uid,
                        Specialized specialized){
        Page<Specialized> pageInfo = PageHelper.startPage(page, limit, "time desc");
        List<Specialized> list = specializedService.findAll(specialized);
        List<Specialized> allList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            Specialized specialized1 = list.get(i);
            Sc s = new Sc();
            s.setTid(list.get(i).getId());
            s.setUid(uid);
            Sc sc = scservice.check(s);
            Comparison com = new Comparison();
            com.setTid(list.get(i).getId());
            com.setUid(uid);
            Comparison comparison = comparisonService.check(com);
            if (comparison!=null){
                specialized1.setDbcode("200");
            }
            else {
                specialized1.setDbcode("500");
            }
            if (sc!=null){
                specialized1.setSccode("200");
            }
            else {
                specialized1.setSccode("500");
            }
            allList.add(specialized1);
        }
        PageInfo<Specialized> all = new PageInfo<>(allList);
        return new PageRet(0, "", all.getList(), (int) all.getTotal());
    }
    //新增数据
    @RequestMapping("/add")
    @ResponseBody
    public JsonObject save(Specialized specialized) {
        try {
            specialized.setId(IdUtil.simpleUUID());
            specialized.setTime(Time.getTime());
            specializedService.save(specialized);
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
                specializedService .del(id);
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
            Specialized u = specializedService.get(id);
            return new JsonObject("200", "", u);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }
    //    更新
    @RequestMapping("/update")
    @ResponseBody
    public JsonObject update(Specialized specialized) {
        try {
            specializedService.update(specialized);
            System.out.println("更新数据");
            return new JsonObject("200", "", null);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }

    }

    @RequestMapping("findAll")
    @ResponseBody
    public JsonObject findAll(Specialized specialized){
        try {

            List<Specialized> list = specializedService.findAll(specialized);

            return new JsonObject("200","", list);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }
    }
    @Resource
    SchoolService schoolService;
    @RequestMapping("findEacharts")
    @ResponseBody
    public JsonObject findEacharts(Specialized specialized){
        try {
            School school = schoolService.get(specialized.getTid());

            List<Specialized> list = specializedService.findeact(specialized);
            List<String>listx = new ArrayList<>();
            List<String>listy = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                listx.add(list.get(i).getYear());
                listy.add(list.get(i).getScore());
            }
            Ea ea = new Ea();
            ea.setListx(listx);
            ea.setListy(listy);
            return new JsonObject("200",school.getName(), ea);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }
    }
    public class Ea{
        private List<String>listx;

        public List<String> getListx() {
            return listx;
        }

        public void setListx(List<String> listx) {
            this.listx = listx;
        }

        public List<String> getListy() {
            return listy;
        }

        public void setListy(List<String> listy) {
            this.listy = listy;
        }

        private List<String>listy;
    }
    @RequestMapping("findgroup")
    @ResponseBody
    public JsonObject findgroup(Specialized specialized){
        try {

            List<Specialized> list = specializedService.findgroup(specialized);

            return new JsonObject("200","", list);
        }
        catch (Exception e){
            return new JsonObject("500","失败","");
        }
    }
//    @RequestMapping("findrecom")
//    @ResponseBody
//    public JsonObject findrecom(Specialized specialized){
//        try {
//
//        if (specialized.getAddress() == null || specialized.getAddress().equals("null")){
//            specialized.setAddress("");
//        }
//        String modifiedString = specialized.getName().substring(1, specialized.getName().length() - 1);
//        specialized.setName(modifiedString);
//        List<Specialized> list = specializedService.findrecom(specialized);
//            return new JsonObject("200","", list);
//        }
//        catch (Exception e){
//            return new JsonObject("500","失败","");
//        }
//    }
    @RequestMapping(value = "/findrecom")
    @ResponseBody
    public PageRet findrecom(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "limit", defaultValue = "2") int limit,
                        Specialized specialized){
        Page<Specialized> pageInfo = PageHelper.startPage(page, limit, "");
        if (specialized.getAddress() == null || specialized.getAddress().equals("null")){
            specialized.setAddress("");
        }
        String modifiedString = specialized.getName().substring(1, specialized.getName().length() - 1);
        specialized.setName(modifiedString);
        List<Specialized> list = specializedService.findrecom(specialized);
        PageInfo<Specialized> all = new PageInfo<>(list);
        return new PageRet(0, "", all.getList(), (int) all.getTotal());
    }
    public static String getDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdFormat = new SimpleDateFormat(format);

        return sdFormat.format(date);
    }
    @RequestMapping(value = "/importTemplate", method = RequestMethod.GET)
    public void importTemplate(HttpServletResponse response,Specialized specialized)throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();
        if (specialized.getAddress() == null || specialized.getAddress().equals("null")){
            specialized.setAddress("");
        }
        if (specialized.getName() != null && specialized.getAddress().equals("")){
            String modifiedString = specialized.getName().substring(1, specialized.getName().length() - 1);
            specialized.setName(modifiedString);

        }
        List<Specialized> list = specializedService.findrecom(specialized);
        HSSFSheet sheet = workbook.createSheet("学校表");
        String dateString = getDate(new Date(), "yyyyMMdd");
        String fileName = dateString+ ".xls";
        int rowNum = 1;
        String[] headers = { "学校", "公办/民办","本科/专科", "专业", "年份","分数"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //在表中存放查询到的数据放入对应的列
        for (Specialized table : list) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(table.getSname());
            row1.createCell(1).setCellValue(table.getType());
            row1.createCell(2).setCellValue(table.getSctype());
            row1.createCell(3).setCellValue(table.getName());
            row1.createCell(4).setCellValue(table.getYear());
            row1.createCell(5).setCellValue(table.getScore());
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());

    }

}

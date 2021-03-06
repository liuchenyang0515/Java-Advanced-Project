package com.me.controller;

import com.me.pojo.Stu;
import com.me.service.StuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
// ApiIgnore就会在Swagger2中不显示该类中所有接口
@ApiIgnore
@RestController
public class StuFooController {
    @Resource
    private StuService stuService;

    @GetMapping("/getStu/{id}")
    public Object getStu(@PathVariable("id") int id) {
        return stuService.getStuInfo(id);
    }


    @PostMapping("/saveStu")
    public Object saveStu() {
        stuService.saveStu();
        return "OK";
    }

    @PostMapping("/updateStu")
    public Object updateStu(int id) {
        stuService.updateStu(id);
        return "OK";
    }

    @PostMapping("/deleteStu")
    public Object deleteStu(int id) {
        stuService.deleteStu(id);
        return "OK";
    }
}

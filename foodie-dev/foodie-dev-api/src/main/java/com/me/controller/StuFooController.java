package com.me.controller;

import com.me.pojo.Stu;
import com.me.service.StuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StuFooController {
    @Resource
    private StuService stuService;

    @GetMapping("/getStu/{id}")
    public Object getStu(@PathVariable("id") int id) {
        return stuService.getStuInfo(id);
    }
}

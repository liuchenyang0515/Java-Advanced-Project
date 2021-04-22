package com.me.controller;

import com.me.enums.YesOrNo;
import com.me.pojo.Carousel;
import com.me.service.CarouselService;
import com.me.utils.ModelJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Resource
    private CarouselService carouselService;


    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ModelJSONResult carousel() {
        List<Carousel> list = carouselService.queryAll(YesOrNo.YES.getCode());
        return ModelJSONResult.ok(list);
    }
}

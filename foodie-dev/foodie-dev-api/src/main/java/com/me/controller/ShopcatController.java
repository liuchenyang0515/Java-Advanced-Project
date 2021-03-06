package com.me.controller;

import com.me.pojo.bo.ShopcartBO;
import com.me.utils.ModelJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Api(value = "购物车接口controller", tags = {"购物车接口相关的api"})
@RequestMapping("shopcart")
@RestController
public class ShopcatController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public ModelJSONResult add(@RequestParam String userId, @RequestBody ShopcartBO shopcartBO,
                               HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return ModelJSONResult.errorMsg("");
        }
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        System.out.println(shopcartBO);
        return ModelJSONResult.ok();
    }


    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public ModelJSONResult del(@RequestParam String userId, @RequestBody String itemSpecId,
                               HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return ModelJSONResult.errorMsg("参数不能为空");
        }
        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的数据
        return ModelJSONResult.ok();
    }
}

package com.me.controller;

import com.me.pojo.UserAddress;
import com.me.service.AddressService;
import com.me.utils.ModelJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RequestMapping("address")
public class AddressController {
    @Resource
    private AddressService addressService;

    /**
     * 用户在确认订单页面，可以针对收获地址做如下操作
     * 1.新增用户的所有收获地址列表
     * 2.新增收获地址
     * 3.删除收获地址
     * 4.修改收获地址
     * 5.设置默认地址
     */
    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public ModelJSONResult list(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return ModelJSONResult.errorMsg("");
        }
        List<UserAddress> list = addressService.queryAll(userId);
        return ModelJSONResult.ok(list);
    }
}

package com.me.controller;

import com.me.enums.PayMethod;
import com.me.pojo.UserAddress;
import com.me.pojo.bo.AddressBO;
import com.me.pojo.bo.SubmitOrderBO;
import com.me.service.AddressService;
import com.me.utils.MobileEmailUtils;
import com.me.utils.ModelJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
public class OrdersController {
    Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public ModelJSONResult create(@RequestBody SubmitOrderBO submitOrderBO) {
        logger.info(submitOrderBO.toString());
        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.getType()
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.getType()) {
            return ModelJSONResult.errorMsg("支付方式不支持");
        }
        // 1.创建订单
        // 2.创建订单以后，移除购物车中已结算(已提交)的商品
        // 3.向支付中心发送当前订单，用于保存支付中心的订单数据
        return ModelJSONResult.ok();
    }
}

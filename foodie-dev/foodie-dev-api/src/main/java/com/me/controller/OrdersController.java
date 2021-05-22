package com.me.controller;

import com.me.enums.OrderStatusEnum;
import com.me.enums.PayMethod;
import com.me.pojo.bo.SubmitOrderBO;
import com.me.service.OrderService;
import com.me.utils.CookieUtils;
import com.me.utils.ModelJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
public class OrdersController extends BaseController {
    Logger logger = LoggerFactory.getLogger(OrdersController.class);
    @Resource
    private OrderService orderService;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public ModelJSONResult create(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response) {
        logger.info(submitOrderBO.toString());
        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.getType()
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.getType()) {
            return ModelJSONResult.errorMsg("支付方式不支持");
        }
        // 1.创建订单
        String orderId = orderService.createOrder(submitOrderBO);
        // 2.创建订单以后，移除购物车中已结算(已提交)的商品
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);
        // 3.向支付中心发送当前订单，用于保存支付中心的订单数据
        return ModelJSONResult.ok(orderId);
    }

    /**
     * 根据商品订单号，更改订单状态
     * @param merchantOrderId
     * @return
     */
    @PostMapping("notifyMerchantOrderPaied")
    public Integer notifyMerchantOrderPaied(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.getCode());
        return HttpStatus.OK.value();
    }
}

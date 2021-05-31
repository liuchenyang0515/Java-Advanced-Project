package com.me.controller;

import org.springframework.stereotype.Controller;

/**
 * 通用化的参数可以写在这里
 */
@Controller
public class BaseController {
    public static final String FOODIE_SHOPCART = "shopcart";
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                        |-> 回调用纸的url
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaied";
}

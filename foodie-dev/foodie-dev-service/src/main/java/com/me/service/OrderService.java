package com.me.service;

import com.me.pojo.bo.SubmitOrderBO;

public interface OrderService {
    /**
     * 用于创建订单相关信息
     *
     * @param submitOrderBO
     */
    public void createOrder(SubmitOrderBO submitOrderBO);
}

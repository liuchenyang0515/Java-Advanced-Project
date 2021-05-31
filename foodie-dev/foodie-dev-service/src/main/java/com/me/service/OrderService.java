package com.me.service;

import com.me.pojo.bo.SubmitOrderBO;
import com.me.pojo.vo.OrderVO;

public interface OrderService {
    /**
     * 用于创建订单相关信息
     *
     * @param submitOrderBO
     */
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);
}

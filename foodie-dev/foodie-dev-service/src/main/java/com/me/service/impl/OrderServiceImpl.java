package com.me.service.impl;

import com.me.enums.YesOrNo;
import com.me.mapper.OrdersMapper;
import com.me.pojo.Orders;
import com.me.pojo.UserAddress;
import com.me.pojo.bo.SubmitOrderBO;
import com.me.service.AddressService;
import com.me.service.OrderService;
import org.apache.commons.lang3.text.WordUtils;
import org.n3r.idworker.Sid;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

public class OrderServiceImpl implements OrderService {
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private AddressService addressService;
    @Resource
    private Sid sid;

    /**
     * 用于创建订单相关信息
     *
     * @param submitOrderBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        // 包邮费用为0
        Integer postAmount = 0;
        String orderId = sid.nextShort();
        UserAddress address = addressService.queryUserAddress(userId, addressId);
        // 1.新订单数据保存
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() + " " + address.getCity()
                + " " + address.getDistrict() + " " + address.getDetail());
//        newOrder.setTotalAmount();
//        newOrder.setRealPayAmount();
        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);

        newOrder.setIsComment(YesOrNo.NO.getCode());
        newOrder.setIsDelete(YesOrNo.NO.getCode());
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());
        // 2.循环根据itemSpecIds保存订单商品信息表
        // 3.保存订单状态表
    }
}

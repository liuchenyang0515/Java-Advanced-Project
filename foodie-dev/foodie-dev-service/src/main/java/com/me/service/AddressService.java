package com.me.service;

import com.me.pojo.UserAddress;

import java.util.List;

public interface AddressService {
    /**
     * 根据用户id查询用户的收货地址列表
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);
}

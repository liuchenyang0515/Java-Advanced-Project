package com.me.service.impl;

import com.me.mapper.UserAddressMapper;
import com.me.pojo.UserAddress;
import com.me.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private UserAddressMapper userAddressMapper;

    /**
     * 根据用户id查询用户的收货地址列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress ua = new UserAddress();
        ua.setUserId(userId);
        return userAddressMapper.select(ua);
    }
}

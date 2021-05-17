package com.me.service.impl;

import com.me.enums.YesOrNo;
import com.me.mapper.UserAddressMapper;
import com.me.pojo.UserAddress;
import com.me.pojo.bo.AddressBO;
import com.me.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private UserAddressMapper userAddressMapper;
    @Resource
    private Sid sid;

    /**
     * 根据用户id查询用户的收货地址列表
     *
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress ua = new UserAddress();
        ua.setUserId(userId);
        return userAddressMapper.select(ua);
    }

    /**
     * 用户新增地址
     *
     * @param addressBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        // 1.判断当前用户是否存在地址，如果没有，则新增为"默认地址"
        Integer isDefault = 0;
        List<UserAddress> addressList = this.queryAll(addressBO.getUserId());
        // ArrayList的isEmpty就是判断size是否为0
        if (addressList == null || addressList.isEmpty()) {
            isDefault = 1;
        }
        String addressId = sid.nextShort();
        // 2.保存地址到数据库
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newAddress);
        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(newAddress);
    }

    /**
     * 用户修改地址
     *
     * @param addressBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();

        UserAddress pendingAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, pendingAddress);

        pendingAddress.setId(addressId);
        pendingAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(pendingAddress);
    }

    /**
     * 根据用户id和地址id，删除对应的用户地址信息
     *
     * @param userId
     * @param addressId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setId(addressId);
        address.setUserId(userId);

        userAddressMapper.delete(address);
    }

    /**
     * 修改默认地址
     *
     * @param userId
     * @param addressId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        // 1.查找默认地址，设置为不默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.getCode());
        List<UserAddress> list = userAddressMapper.select(queryAddress);
        for (UserAddress ua : list) {
            ua.setIsDefault(YesOrNo.NO.getCode());
            userAddressMapper.updateByPrimaryKeySelective(ua);
        }
        // 2.根据地址id修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNo.YES.getCode());
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    /**
     * 根据用户id和地址id，查询具体用户地址对象信息
     * INFO  ServiceLogAspect:41 - ====== 开始执行 class com.me.service.impl.AddressServiceImpl.queryUserAddress ======
     * Creating a new SqlSession
     * Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@34a6c3b8]
     * JDBC Connection [HikariProxyConnection@256319015 wrapping com.mysql.cj.jdbc.ConnectionImpl@15654123] will be managed by Spring
     * ==>  Preparing: SELECT id,user_id,receiver,mobile,province,city,district,detail,extand,is_default,created_time,updated_time FROM
     * user_address WHERE id = ? AND user_id = ?
     * ==> Parameters: 2105146KNR84Z06W(String), 210415DH3FDGZMW0(String)
     * <==    Columns: id, user_id, receiver, mobile, province, city, district, detail, extand, is_default, created_time, updated_time
     * <==        Row: 2105146KNR84Z06W, 210415DH3FDGZMW0, 测试了啊, 13512345678, 广东, 广州, 越秀区, 123123, null, 1, 2021-05-14 09:17:35,
     * 2021-05-14 10:37:04
     * <==      Total: 1
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@34a6c3b8]
     * INFO  ServiceLogAspect:60 - ====== 执行结束，耗时：20 毫秒 ======
     *
     * @param userId
     * @param addressId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress singleAddress = new UserAddress();
        singleAddress.setId(addressId);
        singleAddress.setUserId(userId);
        return userAddressMapper.selectOne(singleAddress);
    }
}

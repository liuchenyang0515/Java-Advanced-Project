package com.me.controller;

import com.me.pojo.UserAddress;
import com.me.pojo.bo.AddressBO;
import com.me.service.AddressService;
import com.me.utils.MobileEmailUtils;
import com.me.utils.ModelJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public ModelJSONResult add(@RequestBody AddressBO addressBO) {
        ModelJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }
        addressService.addNewUserAddress(addressBO);
        return ModelJSONResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public ModelJSONResult delete(
            @RequestParam String userId,
            @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ModelJSONResult.errorMsg("");
        }
        addressService.deleteUserAddress(userId, addressId);
        return ModelJSONResult.ok();
    }


    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefault")
    public ModelJSONResult setDefault(
            @RequestParam String userId,
            @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ModelJSONResult.errorMsg("");
        }
        addressService.updateUserAddressToBeDefault(userId, addressId);
        return ModelJSONResult.ok();
    }

    /**
     * 此段校验参数的函数已经预先写好，直接复制，其实用@Valid更简单
     *
     * @param addressBO
     * @return
     */
    private ModelJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return ModelJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return ModelJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return ModelJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return ModelJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return ModelJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return ModelJSONResult.errorMsg("收货地址信息不能为空");
        }

        return ModelJSONResult.ok();
    }

    @ApiOperation(value = "用户更新地址", notes = "用户更新地址", httpMethod = "POST")
    @PostMapping("/update")
    public ModelJSONResult update(@RequestBody AddressBO addressBO) {
        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return ModelJSONResult.errorMsg("修改地址错误：addressId不能为空");
        }
        ModelJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }
        addressService.updateUserAddress(addressBO);
        return ModelJSONResult.ok();
    }
}

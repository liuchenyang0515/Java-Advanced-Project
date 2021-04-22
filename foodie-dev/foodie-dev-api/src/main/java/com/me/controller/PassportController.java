package com.me.controller;

import com.me.pojo.Users;
import com.me.pojo.bo.UserBO;
import com.me.service.UserService;
import com.me.utils.CookieUtils;
import com.me.utils.JsonUtils;
import com.me.utils.MD5Utils;
import com.me.utils.ModelJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// tags就是在Swagger2中将接口集合重新命名
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Resource
    private UserService userService;

    // 阐述当前方法含义，显示再Swagger2
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public ModelJSONResult usernameIsExist(@RequestParam String username) {

        // 1. 判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return ModelJSONResult.errorMsg("用户名不能为空");
        }

        // 2. 查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return ModelJSONResult.errorMsg("用户名已经存在");
        }

        // 3. 请求成功，用户名没有重复
        return ModelJSONResult.ok();
    }

    // 阐述当前方法含义，显示再Swagger2
    @ApiOperation(value = "用户名注册", notes = "用户名注册", httpMethod = "POST")
    @PostMapping("/register")
    public ModelJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        // 测试withCredentials时, 调试看看Cookie[]
//        Cookie[] cookies = request.getCookies();
        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPwd)) {
            return ModelJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return ModelJSONResult.errorMsg("用户名已经存在");
        }

        // 2. 密码长度不能少于6位
        if (password.length() < 6) {
            return ModelJSONResult.errorMsg("密码长度不能少于6");
        }

        // 3. 判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return ModelJSONResult.errorMsg("两次密码输入不一致");
        }

        // 4. 实现注册
        Users userResult = userService.createUser(userBO);

        // 注册之后设置cookie，这样注册之后就直接能跳转登录，这符合我们日常逻辑，而不是注册后再手动登录一次
        userResult = setNullProperty(userResult);
        /**
         * 前端注册之后会跳转首页index.html，在created方法里会获取名为"user"的cookie，如果是对象，那么会取出cookie里面的昵称和头像信息并显示
         * 如果尝试取出的是空串，那么跳转到的首页就不会显示用户信息，这是不成功的，
         * 所以我们需要在注册成功的最后设置cookie信息并返回，就和用户登录一样，这样注册之后直接就成功登录并跳转到首页。
         * 这部分代码可以和前端代码结合一起看，深刻体会。
         *
         */
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据

        return ModelJSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public ModelJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return ModelJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 实现登录
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));

        if (userResult == null) {
            return ModelJSONResult.errorMsg("用户名或密码不正确");
        }

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);
        return ModelJSONResult.ok(userResult);
    }

    /**
     * cookie的值只是用URLEncoder.encode(cookieValue, "utf-8")编码过的，decode就能解码会的原始信息，
     * 所以清空Users一些属性，只保留Id、username、nickname、sex、face等属性，这样做是为了安全性。
     * 后端URLEncoder.encode(cookieValue, "utf-8")和前端encodeURIComponent一个效果。
     * 前端decodeURIComponent就可以解析出cookie对象。
     *
     * @param userResult
     * @return
     */
    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public ModelJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 清除用户的相关信息的cookie, 可以学习一下CookieUtils处理逻辑
        CookieUtils.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据

        return ModelJSONResult.ok();
    }

}

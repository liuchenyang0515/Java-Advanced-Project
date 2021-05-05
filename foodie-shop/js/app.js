window.app = {
    /* 开发环境 */
    serverUrl: "http://localhost:8088",                                   // 接口服务接口地址
    paymentServerUrl: "http://192.168.1.3:8089",                            // 支付中心服务地址
    shopServerUrl: "http://localhost:8080/foodie-shop/",                  // 门户网站地址
    centerServerUrl: "http://localhost:8080/foodie-center/",              // 用户中心地址
    cookieDomain: "",                                                       // cookie 域

    /* 生产环境 */
    // serverUrl: "http://api.z.mukewang.com:8088/foodie-dev-api",                      // 接口服务接口地址
    // paymentServerUrl: "http://payment.t.mukewang.com/foodie-payment",       // 支付中心服务地址
    // shopServerUrl: "http://shop.z.mukewang.com:8080/foodie-shop/",                            // 门户网站地址
    // centerServerUrl: "http://center.z.mukewang.com:8080/foodie-center/",                        // 用户中心地址
    // cookieDomain: ".z.mukewang.com;",                                       // cookie 域

    ctx: "/foodie-shop",

    getCookie: function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            // console.log(c)
            while (c.charAt(0) == ' ') c = c.substring(1);
            if (c.indexOf(name) != -1) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    },

    goErrorPage() {
        window.location.href = "http://www.imooc.com/error/noexists";
    },

    /**
     * name = "shopcart"
     * value = [{"itemId":"cake-1006","itemImgUrl":"http://122.152.205.72:88/foodie/cake-1006/img2.png",
     * "itemName":"【天天吃货】机器猫最爱 铜锣烧 最美下午茶","specId":"cake-1006-spec-1","specName":"巧克力",
     * "buyCounts":3,"priceDiscount":11700,"priceNormal":13000}]
     *
     * @param name
     * @param value
     */
    setCookie: function (name, value) {
        var Days = 365;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        // shopcart=%5B%7B%22itemId%22%3A%22cake-1006%22%2C%22itemImgUrl%22%3A%22http%3A%2F%2F122.152.205.72%3A88%2Ffoodie%2Fcake-1006%2Fimg2.png%22%2C%22itemName%22%3A%22%E3%80%90%E5%A4%A9%E5%A4%A9%E5%90%83%E8%B4%A7%E3%80%91%E6%9C%BA%E5%99%A8%E7%8C%AB%E6%9C%80%E7%88%B1%20%E9%93%9C%E9%94%A3%E7%83%A7%20%E6%9C%80%E7%BE%8E%E4%B8%8B%E5%8D%88%E8%8C%B6%22%2C%22specId%22%3A%22cake-1006-spec-1%22%2C%22specName%22%3A%22%E5%B7%A7%E5%85%8B%E5%8A%9B%22%2C%22buyCounts%22%3A3%2C%22priceDiscount%22%3A11700%2C%22priceNormal%22%3A13000%7D%5D;path=/;
        // 这里最后有个path=/;
        var cookieContent = name + "=" + encodeURIComponent(value) + ";path=/;";
        if (this.cookieDomain != null && this.cookieDomain != undefined && this.cookieDomain != '') {
            cookieContent += "domain=" + this.cookieDomain;
        }
        document.cookie = cookieContent;
        // document.cookie = name + "="+ encodeURIComponent (value) + ";path=/;domain=" + cookieDomain;//expires=" + exp.toGMTString();
    },

    deleteCookie: function (name) {
        var cookieContent = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        if (this.cookieDomain != null && this.cookieDomain != undefined && this.cookieDomain != '') {
            cookieContent += "domain=" + this.cookieDomain;
        }
        document.cookie = cookieContent;
    },

    getUrlParam(paramName) {
        var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");    //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);            //匹配目标参数
        if (r != null) return decodeURI(r[2]);
        return null;             //返回参数值
    },

    /**
     * 构建购物车商品对象
     */
    ShopcartItem: function (itemId, itemImgUrl, itemName, specId, specName, buyCounts, priceDiscount, priceNormal) {
        this.itemId = itemId;
        this.itemImgUrl = itemImgUrl;
        this.itemName = itemName;
        this.specId = specId;
        this.specName = specName;
        this.buyCounts = buyCounts;
        this.priceDiscount = priceDiscount;
        this.priceNormal = priceNormal;
    },

    /**
     * 传入的pendingItem
     *
     * pendingItem = {
            "itemId": "cake-1006",
            "itemImgUrl": "http://122.152.205.72:88/foodie/cake-1006/img2.png",
            "itemName": "【天天吃货】机器猫最爱 铜锣烧 最美下午茶",
            "specId": "cake-1006-spec-1",
            "specName": "巧克力",
            "buyCounts": 1,
            "priceDiscount": 11700,
            "priceNormal": 13000
        }
     * @param pendingItem
     */
    addItemToShopcart(pendingItem) {
        // 判断有没有购物车，如果没有购物车，则new 一个购物车list
        // 如果有购物车，则直接把shopcartItem丢进去
        var foodieShopcartCookie = this.getCookie("shopcart");
        var foodieShopcart = [];
        if (foodieShopcartCookie != null && foodieShopcartCookie != "" && foodieShopcartCookie != undefined) {
            // 比如：[{"itemId":"cake-1006","itemImgUrl":"http://122.152.205.72:88/foodie/cake-1006/img2.png",
            var foodieShopcartStr = decodeURIComponent(foodieShopcartCookie);
            /**
             * 将具有json规则的字符串转换为JSONObject
             * foodieShopcart = [
                {
                    "itemId": "cake-1006",
                    "itemImgUrl": "http://122.152.205.72:88/foodie/cake-1006/img2.png",
                    "itemName": "【天天吃货】机器猫最爱 铜锣烧 最美下午茶",
                    "specId": "cake-1006-spec-1",
                    "specName": "巧克力",
                    "buyCounts": 2,
                    "priceDiscount": 11700,
                    "priceNormal": 13000
                }
             ]
             * @type {any}
             */
            foodieShopcart = JSON.parse(foodieShopcartStr);

            // 如果不是对象，则重新复制为空数组
            if (typeof (foodieShopcart) != "object") {
                foodieShopcart = [];
            }

            var isHavingItem = false;
            // 如果添加的商品已经存在与购物车中，则购物车中已经存在的商品数量累加新增的
            for (var i = 0; i < foodieShopcart.length; i++) {
                var tmpItem = foodieShopcart[i];
                var specId = tmpItem.specId;
                // 购物车已有该商品
                if (specId == pendingItem.specId) {
                    isHavingItem = true;
                    // 原有该商品数量 + 加入的商品数量
                    var newCounts = tmpItem.buyCounts + pendingItem.buyCounts;
                    tmpItem.buyCounts = newCounts; // newCounts = 3
                    // 删除主图在数组中的位置
                    foodieShopcart.splice(i, 1, tmpItem);
                }
            }
            if (!isHavingItem) {
                foodieShopcart.push(pendingItem);
            }
        } else {
            foodieShopcart.push(pendingItem);
        }

        this.setCookie("shopcart", JSON.stringify(foodieShopcart));
    },

    /**
     * 获得购物车中的数量
     */
    getShopcartItemCounts() {
        // 判断有没有购物车，如果没有购物车，则new 一个购物车list
        // 如果有购物车，则直接把shopcartItem丢进去
        var foodieShopcartCookie = this.getCookie("shopcart");
        var foodieShopcart = [];
        if (foodieShopcartCookie != null && foodieShopcartCookie != "" && foodieShopcartCookie != undefined) {
            var foodieShopcartStr = decodeURIComponent(foodieShopcartCookie);
            foodieShopcart = JSON.parse(foodieShopcartStr);

            // 如果不是对象，则重新复制为空数组
            if (typeof (foodieShopcart) != "object") {
                foodieShopcart = [];
            }
        }
        return foodieShopcart.length;
    },

    /**
     * 获得购物车列表
     */
    getShopcartList() {
        // 判断有没有购物车，如果没有购物车，则new 一个购物车list
        // 如果有购物车，则直接把shopcartItem丢进去
        var foodieShopcartCookie = this.getCookie("shopcart");
        var foodieShopcart = [];
        if (foodieShopcartCookie != null && foodieShopcartCookie != "" && foodieShopcartCookie != undefined) {
            var foodieShopcartStr = decodeURIComponent(foodieShopcartCookie);
            foodieShopcart = JSON.parse(foodieShopcartStr);

            // 如果不是对象，则重新复制为空数组
            if (typeof (foodieShopcart) != "object") {
                foodieShopcart = [];
            }
        }
        return foodieShopcart;
    },

    checkMobile(mobile) {
        var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
        if (!myreg.test(mobile)) {
            return false;
        }
        return true;
    },
}

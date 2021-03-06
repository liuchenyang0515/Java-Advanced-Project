package com.me.pojo.vo;

import java.util.Date;

/**
 * 用于展示商品搜索列表结果的VO
 */
public class SearchItemsVO {
    // 商品id
    private String itemId;
    // 商品名称
    private String itemName;
    // 商品销量
    private int sellCounts;
    // 商品图片url
    private String imgUrl;
    // 商品价格,以分为单位
    private int price;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(int sellCounts) {
        this.sellCounts = sellCounts;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

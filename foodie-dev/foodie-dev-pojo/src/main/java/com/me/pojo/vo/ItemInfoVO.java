package com.me.pojo.vo;

import com.me.pojo.Items;
import com.me.pojo.ItemsImg;
import com.me.pojo.ItemsParam;
import com.me.pojo.ItemsSpec;

import java.util.List;

/**
 * 商品详情VO
 */
public class ItemInfoVO {
    // 商品对象
    private Items item;
    // 商品图片对象列表
    private List<ItemsImg> itemImgList;
    // 商品规格对象列表
    private List<ItemsSpec> itemSpecList;
    // 商品参数对象
    private ItemsParam itemParams;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }
}

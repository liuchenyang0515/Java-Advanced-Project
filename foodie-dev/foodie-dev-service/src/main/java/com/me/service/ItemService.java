package com.me.service;

import com.me.pojo.Items;
import com.me.pojo.ItemsImg;
import com.me.pojo.ItemsParam;
import com.me.pojo.ItemsSpec;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品ID查询详情
     *
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表
     *
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     *
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     *
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);
}

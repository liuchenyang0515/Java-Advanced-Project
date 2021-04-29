package com.me.mapper;

import com.me.pojo.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {
    /**
     * 查询用户评论
     * @param map map含有商品的itemId参数和评价等级level（好评中评还是差评）
     * @return
     */
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);
}
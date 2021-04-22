package com.me.service;

import com.me.pojo.Carousel;

import java.util.List;

public interface CarouselService {
    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(Integer isShow);
}

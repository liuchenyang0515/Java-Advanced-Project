package com.me.service;


import com.me.pojo.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有一级分类
     *
     * @return
     */
    public List<Category> queryAllRootLevelCat();
}

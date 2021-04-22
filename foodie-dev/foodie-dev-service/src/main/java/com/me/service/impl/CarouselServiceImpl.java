package com.me.service.impl;

import com.me.mapper.CarouselMapper;
import com.me.pojo.Carousel;
import com.me.service.CarouselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Resource
    private CarouselMapper carouselMapper;

    /**
     * INFO  ServiceLogAspect:41 - ====== 开始执行 class com.me.service.impl.CarouselServiceImpl.queryAll ======
     * Creating a new SqlSession
     * Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@6e8c25a3]
     * JDBC Connection [HikariProxyConnection@561437753 wrapping com.mysql.cj.jdbc.ConnectionImpl@97a4fa9] will be managed by Spring
     * ==>  Preparing: SELECT id,image_url,background_color,item_id,cat_id,type,sort,is_show,create_time,update_time FROM carousel
     * WHERE ( ( is_show = ? ) ) order by sort DESC
     * ==> Parameters: 1(Integer)
     * <==    Columns: id, image_url, background_color, item_id, cat_id, type, sort, is_show, create_time, update_time
     * <==        Row: c-10021, http://122.152.205.72:88/group1/M00/00/05/CpoxxF0ZmH6AeuRrAAEZviPhyQ0768.png, #55be59, , 133, 2, 4, 1, 2019-08-28 20:33:06, 2019-08-28 20:33:09
     * <==        Row: c-10015, http://122.152.205.72:88/group1/M00/00/05/CpoxxF0ZmHuAPlXvAAFe-H5_-Nw961.png, #ff9801, cake-1006, , 1, 3, 1, 2019-08-26 20:33:06, 2019-08-26 20:33:09
     * <==        Row: c-10013, http://122.152.205.72:88/group1/M00/00/05/CpoxxF0ZmHiAWwR7AAFdqZHw8jU876.png, #000240, , 51, 2, 2, 1, 2019-08-25 20:33:06, 2019-08-25 20:33:09
     * <==        Row: c-10011, http://122.152.205.72:88/group1/M00/00/05/CpoxxF0ZmG-ALsPRAAEX2Gk9FUg848.png, #f44661, nut-1004, , 1, 1, 1, 2019-08-27 20:33:06, 2019-08-24 20:33:09
     * <==      Total: 4
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@6e8c25a3]
     * INFO  ServiceLogAspect:60 - ====== 执行结束，耗时：10 毫秒 ======
     *
     * @param isShow
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAll(Integer isShow) {

        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);

        List<Carousel> result = carouselMapper.selectByExample(example);

        return result;
    }
}

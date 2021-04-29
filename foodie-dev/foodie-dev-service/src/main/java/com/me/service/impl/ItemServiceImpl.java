package com.me.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.me.enums.CommentLevel;
import com.me.mapper.*;
import com.me.pojo.*;
import com.me.pojo.vo.CommentLevelCountsVO;
import com.me.pojo.vo.ItemCommentVO;
import com.me.service.ItemService;
import com.me.utils.PagedGridResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.tree.ReturnType;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private ItemsMapper itemsMapper;
    @Resource
    private ItemsImgMapper itemsImgMapper;
    @Resource
    private ItemsSpecMapper itemsSpecMapper;
    @Resource
    private ItemsParamMapper itemsParamMapper;
    @Resource
    private ItemsCommentsMapper itemsCommentsMapper;
    @Resource
    private ItemsMapperCustom itemsMapperCustom;

    /**
     * 根据商品ID查询详情
     * JDBC Connection [HikariProxyConnection@1058469840 wrapping com.mysql.cj.jdbc.ConnectionImpl@6d7e6084] will be managed by Spring
     * ==>  Preparing: SELECT id,item_name,cat_id,root_cat_id,sell_counts,on_off_status,created_time,updated_time,content FROM items WHERE id = ?
     * ==> Parameters: cake-1001(String)
     * <==    Columns: id, item_name, cat_id, root_cat_id, sell_counts, on_off_status, created_time, updated_time, content
     * <==        Row: cake-1001, 【天天吃货】真香预警 超级好吃 手撕面包 儿童早餐早饭, 37, 1, 1003, 1, 2019-09-09 14:45:34, 2019-09-09 14:45:38, <<BLOB>>
     * <==      Total: 1
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 根据商品id查询商品图片列表
     * <p>
     * Creating a new SqlSession
     * Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@158009e7]
     * JDBC Connection [HikariProxyConnection@1566288098 wrapping com.mysql.cj.jdbc.ConnectionImpl@6d7e6084] will be managed by Spring
     * ==>  Preparing: SELECT id,item_id,url,sort,is_main,created_time,updated_time FROM items_img WHERE ( ( item_id = ? ) )
     * ==> Parameters: cake-1001(String)
     * <==    Columns: id, item_id, url, sort, is_main, created_time, updated_time
     * <==        Row: 1, cake-1001, http://122.152.205.72:88/foodie/cake-1001/img1.png, 0, 1, 2019-07-01 14:46:55, 2019-07-01 14:47:02
     * <==        Row: 2, cake-1001, http://122.152.205.72:88/foodie/cake-1001/img2.png, 1, 0, 2019-07-01 14:46:55, 2019-07-01 14:47:02
     * <==        Row: 3, cake-1001, http://122.152.205.72:88/foodie/cake-1001/img3.png, 2, 0, 2019-07-01 14:46:55, 2019-07-01 14:47:02
     * <==      Total: 3
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@158009e7]
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example itemsImgExp = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsImgExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(itemsImgExp);
    }

    /**
     * 根据商品id查询商品规格
     * <p>
     * ==>  Preparing: SELECT id,item_id,name,stock,discounts,price_discount,price_normal,created_time,updated_time FROM items_spec WHERE ( ( item_id = ? ) )
     * ==> Parameters: cake-1001(String)
     * <==    Columns: id, item_id, name, stock, discounts, price_discount, price_normal, created_time, updated_time
     * <==        Row: 1, cake-1001, 原味, 2276, 0.90, 18000, 20000, 2019-07-01 14:54:20, 2019-07-01 14:54:28
     * <==        Row: 2, cake-1001, 草莓味, 1007, 1.00, 20000, 20000, 2019-07-01 14:54:20, 2019-07-01 14:54:28
     * <==        Row: 3, cake-1001, 香草味, 978, 0.88, 17600, 20000, 2019-07-01 14:54:20, 2019-07-01 14:54:28
     * <==      Total: 3
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example itemsSpecExp = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsSpecExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(itemsSpecExp);
    }

    /**
     * 根据商品id查询商品参数
     * <p>
     * INFO  ServiceLogAspect:41 - ====== 开始执行 class com.me.service.impl.ItemServiceImpl.queryItemParam ======
     * Creating a new SqlSession
     * Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@5ad581fd]
     * JDBC Connection [HikariProxyConnection@7527962 wrapping com.mysql.cj.jdbc.ConnectionImpl@6d7e6084] will be managed by Spring
     * ==>  Preparing: SELECT id,item_id,produc_place,foot_period,brand,factory_name,factory_address,packaging_method,weight,storage_method,
     * eat_method,created_time,updated_time FROM items_param WHERE ( ( item_id = ? ) )
     * ==> Parameters: cake-1001(String)
     * <==    Columns: id, item_id, produc_place, foot_period, brand, factory_name, factory_address, packaging_method, weight, storage_method,
     * eat_method, created_time, updated_time
     * <==        Row: 1, cake-1001, 中国, 180天, 慕课网, 北京慕课网有限公司, 北京中关村, 袋装, 50g, 常温5~25°, 拆开即食, 2019-07-01 15:32:30, 2019-07-01 15:32:35
     * <==      Total: 1
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@5ad581fd]
     * INFO  ServiceLogAspect:60 - ====== 执行结束，耗时：6 毫秒 ======
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example itemsParamExp = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsParamExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(itemsParamExp);
    }

    /**
     * 根据商品id查询商品的评价等级数量
     * <p>
     * <p>
     * INFO  ServiceLogAspect:41 - ====== 开始执行 class com.me.service.impl.ItemServiceImpl.queryCommentCounts ======
     * Creating a new SqlSession
     * Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3e0c04e9]
     * JDBC Connection [HikariProxyConnection@118868182 wrapping com.mysql.cj.jdbc.ConnectionImpl@63949a7] will be managed by Spring
     * ==>  Preparing: SELECT COUNT(id) FROM items_comments WHERE item_id = ? AND comment_level = ?
     * ==> Parameters: cake-1001(String), 1(Integer)
     * <==    Columns: COUNT(id)
     * <==        Row: 14
     * <==      Total: 1
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3e0c04e9]
     * Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3e0c04e9] from current transaction
     * ==>  Preparing: SELECT COUNT(id) FROM items_comments WHERE item_id = ? AND comment_level = ?
     * ==> Parameters: cake-1001(String), 2(Integer)
     * <==    Columns: COUNT(id)
     * <==        Row: 7
     * <==      Total: 1
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3e0c04e9]
     * Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3e0c04e9] from current transaction
     * ==>  Preparing: SELECT COUNT(id) FROM items_comments WHERE item_id = ? AND comment_level = ?
     * ==> Parameters: cake-1001(String), 3(Integer)
     * <==    Columns: COUNT(id)
     * <==        Row: 2
     * <==      Total: 1
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3e0c04e9]
     * INFO  ServiceLogAspect:60 - ====== 执行结束，耗时：7 毫秒 ======
     *
     * @param itemId
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.getCode());
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.getCode());
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.getCode());
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setTotalCounts(totalCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setBadCounts(badCounts);
        return countsVO;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null) {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

    /**
     * 根据商品id查询商品的评价（分页）
     * <p>
     * INFO  ServiceLogAspect:41 - ====== 开始执行 class com.me.service.impl.ItemServiceImpl.queryPagedComments ======
     * Creating a new SqlSession
     * Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@29183d13]
     * JDBC Connection [HikariProxyConnection@1108095743 wrapping com.mysql.cj.jdbc.ConnectionImpl@5e4f68f] will be managed by Spring
     * ==>  Preparing: SELECT count(0) FROM items_comments ic LEFT JOIN users u ON ic.user_id = u.id WHERE ic.item_id = ?
     * ==> Parameters: cake-1001(String)
     * <==    Columns: count(0)
     * <==        Row: 23
     * <==      Total: 1
     * ==>  Preparing: SELECT ic.comment_level AS commentLevel, ic.content AS content, ic.sepc_name AS specName, ic.created_time AS createdTime,
     * u.face AS userFace, u.nickname AS nickname FROM items_comments ic LEFT JOIN users u ON ic.user_id = u.id WHERE ic.item_id = ? LIMIT ?
     * ==> Parameters: cake-1001(String), 10(Integer)
     * <==    Columns: commentLevel, content, specName, createdTime, userFace, nickname
     * <==        Row: 1, 很棒, 草莓味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 2, very good, 草莓味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 3, 非常好吃, 香草味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 1, 非常不错！~, 香草味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 2, 非常好吃, 香草味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 2, 非常好吃, 香草味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 1, 非常好吃, 原味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 1, 非常好吃, 香草味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 1, 非常好吃, 原味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==        Row: 1, 非常好吃, 原味, 2019-07-22 09:55:05, http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png, imooc
     * <==      Total: 10
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@29183d13]
     * INFO  ServiceLogAspect:60 - ====== 执行结束，耗时：10 毫秒 ======
     * Transaction synchronization deregistering SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@29183d13]
     * Transaction synchronization closing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@29183d13]
     *
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);
        // mybatis-pagehelper
        /**
         * 使用分页插件，在查询前使用分页插件，原理：统一拦截sql，为其提供分页功能
         * page: 第几页
         * pageSize：每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVO> list = itemsMapperCustom.queryItemComments(map);

        return setterPagedGrid(list, page);
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page); // 当前页数
        grid.setRows(list); // 每行显示的内容，评论的时间、昵称、头像、购买的规格等等
        grid.setTotal(pageList.getPages()); // 总页数
        grid.setRecords(pageList.getTotal()); // 总记录数
        return grid;
    }

}

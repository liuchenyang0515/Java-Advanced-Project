package com.me.controller;


import com.me.pojo.Items;
import com.me.pojo.ItemsImg;
import com.me.pojo.ItemsParam;
import com.me.pojo.ItemsSpec;
import com.me.pojo.vo.CommentLevelCountsVO;
import com.me.pojo.vo.ItemInfoVO;
import com.me.service.ItemService;
import com.me.utils.ModelJSONResult;
import com.me.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {

    @Resource
    private ItemService itemService;

    /**
     * 给出商品id--itemId，根据id查询该商品详情（商品名称、累计销售、上下架状态）、
     * 该商品图片列表（图片地址、是否主图）、该商品规格列表（规格名、库存、原价、折扣价、折扣力度）、
     * 该商品参数（产地--如中国深圳、保质期、品牌名、生产厂商、产地详细地址、包装方式、规格重量、使用方式、存储方法）
     *
     * @param itemId
     * @return
     */
    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public ModelJSONResult info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return ModelJSONResult.errorMsg(null);
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return ModelJSONResult.ok(itemInfoVO);
    }

    /**
     * 查询评价总数、好评数、中评数、差评数
     *
     * @param itemId
     * @return
     */
    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public ModelJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return ModelJSONResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        return ModelJSONResult.ok(countsVO);
    }

    /**
     * 给出具体的商品id--itemId，然后传入level说明是查询好评中评还是差评，然后选择性查询第page页，每页pageSize条记录
     * 不传page默认第一页，不传pageSize默认每页显示10条记录
     *
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public ModelJSONResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return ModelJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = itemService.queryPagedComments(itemId, level, page, pageSize);

        return ModelJSONResult.ok(grid);
    }


    /**
     * 在前端catItems.html中，调用搜索的时候，就限定了sort="k"&page=1&pageSize=20，如下
     * this.searchInBackend(keywords, "k", 1, 20);
     * 在xml提到
     * <if test=" paramsMap.keywords != null and paramsMap.keywords != '' ">
     * <!-- 语法要求在%%之间拼接不要用#，用$。这里不可能出现sql注入攻击，具体原因见接口说明-->
     * AND i.item_name LIKE '%${paramsMap.keywords}%'
     * </if>
     * 这里不可能出现sql注入攻击，因为keywords是要拼接到url，而url要求如下
     * （1）RFC3986文档规定，Url中只允许包含英文字母（a-z，A-Z）、数字（0-9）、- _ . ~ 4个特殊字符以及所有保留字符。
     * （2）RFC3986中指定了以下字符为保留字符：! * ’ ( ) ; : @ & = + $ , / ? # [ ]
     * 尝试注入报错：Invalid character found in the request target. The valid characters are defined in RFC 7230 and RFC 3986
     *
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public ModelJSONResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {
        // 看个人逻辑，这个keywords为空就不让搜，你也可以改为keywords为空时搜索全部
        if (StringUtils.isBlank(keywords)) {
            return ModelJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(keywords, sort, page, pageSize);
        return ModelJSONResult.ok(grid);
    }


    /**
     * localhost:8088/items/catItems?catId=51&sort=&page=&pageSize
     * 像这种，参数没有赋值，引用类型Integer默认是null，String类型是""
     *
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     * @RequestParam注解默认required是true 目的是要求要有这个属性字段，但是你可以不传值，比如sort=你可以不传，但是你要有sort
     */
    @ApiOperation(value = "通过分类id搜索商品列表", notes = "通过分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public ModelJSONResult catItems(
            @ApiParam(name = "catId", value = "三级分类id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam(required = true) Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (catId == null) {
            return ModelJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(catId, sort, page, pageSize);
        return ModelJSONResult.ok(grid);
    }
}

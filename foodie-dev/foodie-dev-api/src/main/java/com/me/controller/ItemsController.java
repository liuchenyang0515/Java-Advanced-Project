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
}

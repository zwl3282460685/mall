package com.zwl.mall.controller.mall;

import com.zwl.mall.common.Constants;
import com.zwl.mall.controller.vo.NewBeeMallGoodsDetailVO;
import com.zwl.mall.controller.vo.SearchPageCategoryVO;
import com.zwl.mall.entity.NewBeeMallGoods;
import com.zwl.mall.service.NewBeeMallCategoryService;
import com.zwl.mall.service.NewBeeMallGoodsService;
import com.zwl.mall.util.BeanUtil;
import com.zwl.mall.util.PageQueryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class GoodsController {

    @Resource
    NewBeeMallCategoryService newBeeMallCategoryService;

    @Resource
    NewBeeMallGoodsService newBeeMallGoodsService;

    @GetMapping({"/search", "/search.html"})
    public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request){
        if(StringUtils.isEmpty(params.get("page"))){
            params.put("page", 1);
        }
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //封装分类数据
        if(params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")){
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            SearchPageCategoryVO searchPageCategoryVO  = newBeeMallCategoryService.getCategoriesForSearch(categoryId);
            if(searchPageCategoryVO != null){
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
            }
        }
        //封装参数供前端回显
        if(params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")){
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }

        String keyword = "";
        //对keyword做过滤 去掉空格
        if(params.containsKey("keyword") && !StringUtils.isEmpty(params.get("keyword") + "")){
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);

        //封装商品数据
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        request.setAttribute("pageResult", newBeeMallGoodsService.searchNewBeeMallGoods(pageQueryUtil));
        return "mall/search";
    }

    @GetMapping("/goods/detail/{goodsId}")
    public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request){
        if(goodsId < 1){
            return "error/error_5xx";
        }
        NewBeeMallGoods goods = newBeeMallGoodsService.getNewBeeMallGoodsById(goodsId);
        if(goods == null){
            return "error/error_5xx";
        }
        NewBeeMallGoodsDetailVO goodsDetailVO = new NewBeeMallGoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        request.setAttribute("goodsDetail", goodsDetailVO);
        return "mall/detail";
    }
}

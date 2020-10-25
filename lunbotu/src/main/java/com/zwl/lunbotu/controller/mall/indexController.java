package com.zwl.lunbotu.controller.mall;

import com.zwl.lunbotu.common.Constants;
import com.zwl.lunbotu.common.IndexConfigTypeEnum;
import com.zwl.lunbotu.controller.vo.NewBeeMallIndexCarouselVO;
import com.zwl.lunbotu.controller.vo.NewBeeMallIndexCategoryVO;
import com.zwl.lunbotu.controller.vo.NewBeeMallIndexConfigGoodsVO;
import com.zwl.lunbotu.service.NewBeeMallCarouselService;
import com.zwl.lunbotu.service.NewBeeMallCategoryService;
import com.zwl.lunbotu.service.NewBeeMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class indexController {

    @Resource
    private NewBeeMallCategoryService newBeeMallCategoryService;

    @Resource
    private NewBeeMallCarouselService newBeeMallCarouselService;

    @Resource
    private NewBeeMallIndexConfigService newBeeMallIndexConfigService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request){
        List<NewBeeMallIndexCategoryVO> categories = newBeeMallCategoryService.getCategoriesForIndex();
        List<NewBeeMallIndexCarouselVO> carousels = newBeeMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<NewBeeMallIndexConfigGoodsVO> hotGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<NewBeeMallIndexConfigGoodsVO> newGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<NewBeeMallIndexConfigGoodsVO> recommendGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }
}

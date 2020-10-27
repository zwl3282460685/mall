package com.zwl.lunbotu.service;

import com.zwl.lunbotu.controller.vo.NewBeeMallIndexCategoryVO;
import com.zwl.lunbotu.controller.vo.SearchPageCategoryVO;
import com.zwl.lunbotu.entity.GoodsCategory;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.PageResult;

import java.util.List;

public interface NewBeeMallCategoryService {

    PageResult getCategorisPage(PageQueryUtil pageQueryUtil); //商品分页类目列表

    String saveCategory(GoodsCategory goodsCategory); //添加商品类目

    String updateGoodsCategory(GoodsCategory goodsCategory); //更新商品类目

    boolean deleteBatch(Integer[] ids);

    //根据parentId和level获取分类列表
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> singletonList, int level);

    GoodsCategory getGoodCategoryById(Long categoryId);

    //获取首页商品分类数据
    List<NewBeeMallIndexCategoryVO> getCategoriesForIndex();

    //返回分类数据(搜索页调用)
    SearchPageCategoryVO getCategoriesForSearch(Long categoryId);
}

package com.zwl.lunbotu.service;

import com.zwl.lunbotu.entity.NewBeeMallGoods;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.PageResult;

public interface NewBeeMallGoodsService {
    //后台分页
    PageResult getNewBeeMallGoodsPage(PageQueryUtil pageQueryUtil);

    //保存商品
    String saveNewBeeMallGoods(NewBeeMallGoods goods);

    //修改商品
    String updateNewBeeMallGoods(NewBeeMallGoods goods);

    //根据id获取商品详情
    NewBeeMallGoods getNewBeeMallGoodsById(Long id);

    //批量修改销售状态(上架下架)
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil);
}

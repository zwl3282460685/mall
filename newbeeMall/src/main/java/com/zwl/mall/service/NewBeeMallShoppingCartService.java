package com.zwl.mall.service;

import com.zwl.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import com.zwl.mall.entity.NewBeeMallShoppingCartItem;

import java.util.List;

public interface NewBeeMallShoppingCartService {

    //添加商品到购物车
    String saveNewBeeMallCartItem(NewBeeMallShoppingCartItem newBeeMallShoppingCartItem);

    //修改购物车中的属性
    String updateNewBeeMallCartItem(NewBeeMallShoppingCartItem newBeeMallShoppingCartItem);

    //获取我的购物车中的列表数据
    List<NewBeeMallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId);

    //删除购物车中的商品
    Boolean deleteById(Long newBeeMallShoppingCartItemId);
}

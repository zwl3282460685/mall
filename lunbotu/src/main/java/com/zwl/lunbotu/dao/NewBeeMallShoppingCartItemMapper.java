package com.zwl.lunbotu.dao;


import com.zwl.lunbotu.entity.NewBeeMallShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewBeeMallShoppingCartItemMapper {

    NewBeeMallShoppingCartItem selectByUserIdAndGoodsId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("goodsId") Long goodsId);

    int selectCountByUserId(Long newBeeMallUserId);

    int insertSelective(NewBeeMallShoppingCartItem newBeeMallShoppingCartItem);

    int updateByPrimaryKeySelective(NewBeeMallShoppingCartItem record);

    NewBeeMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    List<NewBeeMallShoppingCartItem> selectByUserId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("number") int number);

    Boolean deleteByPrimaryKey(Long newBeeMallShoppingCartItemId);

    int deleteBatch(List<Long> ids);
}

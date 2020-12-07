package com.zwl.mall.dao;

import com.zwl.mall.entity.NewBeeMallGoods;
import com.zwl.mall.entity.StockNumDTO;
import com.zwl.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewBeeMallGoodsMapper {

    int deleteByPrimaryKey(Long goodsId);
    int insert(NewBeeMallGoods record);

    int insertSelective(NewBeeMallGoods record);
    NewBeeMallGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(NewBeeMallGoods record);

    int updateByPrimaryKeyWithBLOBs(NewBeeMallGoods record);

    int updateByPrimaryKey(NewBeeMallGoods record);

    List<NewBeeMallGoods> findNewBeeMallGoodsList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoods(PageQueryUtil pageUtil);

    int batchUpdateSellStatus(@Param("orderIds") Long[] orderIds, @Param("sellStatus") int sellStatus);

    //根据商品 id 列表查询商品记录信息：
    List<NewBeeMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    //查询商品
    List<NewBeeMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);
}

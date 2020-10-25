package com.zwl.lunbotu.dao;

import com.zwl.lunbotu.entity.GoodsCategory;
import com.zwl.lunbotu.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsCategoryMapper {

    List<GoodsCategory> findGoodsCategoryList(PageQueryUtil pageUtil);

    int getTotalGoodsCategories(PageQueryUtil pageQueryUtil);

    GoodsCategory selectByLevelAndName(@Param("categoryLevel") Byte categoryLevel, @Param("categoryName") String categoryName);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Long categoryId);

    int updateByPrimaryKeySelective(GoodsCategory goodsCategory);

    int deleteBatch(Integer[] ids);

    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(@Param("parentsIds") List<Long> parentsIds, @Param("categoryLevel") int categoryLevel, @Param("number") int number);
}

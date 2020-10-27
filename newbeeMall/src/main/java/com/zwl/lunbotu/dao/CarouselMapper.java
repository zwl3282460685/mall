package com.zwl.lunbotu.dao;


import com.zwl.lunbotu.entity.Carousel;
import com.zwl.lunbotu.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarouselMapper {
    int deleteByPrimaryKey(Integer carouselId);

    int insert(Carousel record);
    int insertSelective(Carousel record); //添加轮播图

    Carousel selectByPrimaryKey(Integer carouselId);//根据id进行查找

    int updateByPrimaryKeySelective(Carousel record);//修改数据

    int updateByPrimaryKey(Carousel record);

    List<Carousel> findCarouselList(PageQueryUtil pageUtil);

    int getTotalCarousels(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);

    List<Carousel> findCarouselsByNum(@Param("number") int number);
}

package com.zwl.mall.service;

import com.zwl.mall.controller.vo.NewBeeMallIndexCarouselVO;
import com.zwl.mall.entity.Carousel;
import com.zwl.mall.util.PageQueryUtil;
import com.zwl.mall.util.PageResult;

import java.util.List;

public interface NewBeeMallCarouselService {

    //后台分页
    PageResult getCarouselPage(PageQueryUtil pageQueryUtil);

    //添加轮播图接口
    String savaCarousel(Carousel carousel);

    //修改轮播图接口
    String updateCarousel(Carousel carousel);

    //根据 id 获取单条轮播图记录接口
    Carousel getCarouselById(Integer id);

    //批量删除轮播图接口
    Boolean deleteBatch(Integer[] ids);

    //返回固定数量的轮播图对象(首页调用)
    List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int number);

}

package com.zwl.lunbotu.service.impl;

import com.zwl.lunbotu.common.ServiceResultEnum;
import com.zwl.lunbotu.controller.vo.NewBeeMallIndexCarouselVO;
import com.zwl.lunbotu.dao.CarouselMapper;
import com.zwl.lunbotu.entity.Carousel;
import com.zwl.lunbotu.service.NewBeeMallCarouselService;
import com.zwl.lunbotu.util.BeanUtil;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NewBeeMallCarouselServiceImpl implements NewBeeMallCarouselService {

    @Resource
    private CarouselMapper carouselMapper;

    @Override
    public PageResult getCarouselPage(PageQueryUtil pageQueryUtil) {
        List<Carousel> carouses = carouselMapper.findCarouselList(pageQueryUtil);
        int total = carouselMapper.getTotalCarousels(pageQueryUtil);
        PageResult pageResult = new PageResult(carouses, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String savaCarousel(Carousel carousel) {
        if(carouselMapper.insertSelective(carousel) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCarousel(Carousel carousel) {
        Carousel temp = carouselMapper.selectByPrimaryKey(carousel.getCarouselId());
        if(null == temp){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        temp.setCarouselRank(carousel.getCarouselRank());
        temp.setCarouselUrl(carousel.getCarouselUrl());
        temp.setRedirectUrl(carousel.getRedirectUrl());
        temp.setUpdateTime(new Date());

        if(carouselMapper.updateByPrimaryKeySelective(temp) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Carousel getCarouselById(Integer id) {
        return carouselMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if(ids.length < 1){
            return false;
        }
        return carouselMapper.deleteBatch(ids) > 0;
    }

    //返回固定数量的轮播图对象(首页调用)
    @Override
    public List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int number) {
        List<NewBeeMallIndexCarouselVO> newBeeMallIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if(!CollectionUtils.isEmpty(carousels)){
            newBeeMallIndexCarouselVOS = BeanUtil.copyList(carousels, NewBeeMallIndexCarouselVO.class);
        }
        return newBeeMallIndexCarouselVOS;
    }
}

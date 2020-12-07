package com.zwl.mall.service.impl;

import com.zwl.mall.common.ServiceResultEnum;
import com.zwl.mall.controller.vo.NewBeeMallSearchGoodsVO;
import com.zwl.mall.dao.NewBeeMallGoodsMapper;
import com.zwl.mall.entity.NewBeeMallGoods;
import com.zwl.mall.service.NewBeeMallGoodsService;
import com.zwl.mall.util.BeanUtil;
import com.zwl.mall.util.PageQueryUtil;
import com.zwl.mall.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NewBeeMallGoodsServiceImpl implements NewBeeMallGoodsService {

    @Resource
    private NewBeeMallGoodsMapper goodsMapper;

    @Override
    public PageResult getNewBeeMallGoodsPage(PageQueryUtil pageQueryUtil) {
        List<NewBeeMallGoods> goodsList = goodsMapper.findNewBeeMallGoodsList(pageQueryUtil);
        int total = goodsMapper.getTotalNewBeeMallGoods(pageQueryUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveNewBeeMallGoods(NewBeeMallGoods goods) {
        if(goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNewBeeMallGoods(NewBeeMallGoods goods) {
        NewBeeMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if(temp == null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if(goodsMapper.updateByPrimaryKeySelective(goods) > 0 ){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public NewBeeMallGoods getNewBeeMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    //搜索商品
    @Override
    public PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil) {
        List<NewBeeMallGoods> goodsList = goodsMapper.findNewBeeMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalNewBeeMallGoodsBySearch(pageUtil);
        List<NewBeeMallSearchGoodsVO> newBeeMallSearchGoodsVOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(goodsList)){
            newBeeMallSearchGoodsVOS = BeanUtil.copyList(goodsList, NewBeeMallSearchGoodsVO.class);
            for(NewBeeMallSearchGoodsVO newBeeMallSearchGoodsVO : newBeeMallSearchGoodsVOS){
                String goodsName = newBeeMallSearchGoodsVO.getGoodsName();
                String goodsIntro = newBeeMallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if(goodsName.length() > 28){
                    goodsName = goodsName.substring(0, 28) + "...";
                    newBeeMallSearchGoodsVO.setGoodsName(goodsName);
                }

                if(goodsIntro.length() > 30){
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    newBeeMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(newBeeMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}

package com.zwl.lunbotu.service.impl;

import com.zwl.lunbotu.common.ServiceResultEnum;
import com.zwl.lunbotu.controller.vo.NewBeeMallIndexConfigGoodsVO;
import com.zwl.lunbotu.dao.IndexConfigMapper;
import com.zwl.lunbotu.dao.NewBeeMallGoodsMapper;
import com.zwl.lunbotu.entity.IndexConfig;
import com.zwl.lunbotu.entity.NewBeeMallGoods;
import com.zwl.lunbotu.service.NewBeeMallIndexConfigService;
import com.zwl.lunbotu.util.BeanUtil;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewBeeMallIndexConfigServiceImpl implements NewBeeMallIndexConfigService {

    @Resource
    private IndexConfigMapper indexConfigMapper;

    @Resource
    private NewBeeMallGoodsMapper goodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //判断是否存在该商品
        if(indexConfigMapper.insertSelective(indexConfig) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }else{
            return ServiceResultEnum.DB_ERROR.getResult();
        }
    }

    @Override
    public boolean deleteBatch(Long[] ids) {
        if(ids.length < 1){
            return false;
        }
        return indexConfigMapper.deleteBatch(ids) > 0;
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if(temp == null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if(indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    //返回固定数量的首页配置商品对象(首页调用)
    @Override
    public List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<NewBeeMallIndexConfigGoodsVO> newBeeMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if(!CollectionUtils.isEmpty(indexConfigs)){
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<NewBeeMallGoods> newBeeMallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            newBeeMallIndexConfigGoodsVOS = BeanUtil.copyList(newBeeMallGoods, NewBeeMallIndexConfigGoodsVO.class);
            for(NewBeeMallIndexConfigGoodsVO newBeeMallIndexConfigGoodsVO : newBeeMallIndexConfigGoodsVOS){
                String goodsName = newBeeMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = newBeeMallIndexConfigGoodsVO.getGoodsIntro();

                //字符串过长导致文字超出的问题
                if(goodsName.length() > 30){
                   goodsName = goodsName.substring(0, 30) + "...";
                   newBeeMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }

                if(goodsIntro.length() > 22){
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    newBeeMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return newBeeMallIndexConfigGoodsVOS;
    }
}

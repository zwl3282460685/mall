package com.zwl.lunbotu.service;

import com.zwl.lunbotu.controller.vo.NewBeeMallIndexConfigGoodsVO;
import com.zwl.lunbotu.entity.IndexConfig;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.PageResult;

import java.util.List;

public interface NewBeeMallIndexConfigService {

    //后台分页
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    //添加首页配置
    String saveIndexConfig(IndexConfig indexConfig);

    //删除首页配置
    boolean deleteBatch(Long[] ids);

    //更新首页配置信息
    String updateIndexConfig(IndexConfig indexConfig);

    //返回固定数量的首页配置商品对象(首页调用)
    List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);
}

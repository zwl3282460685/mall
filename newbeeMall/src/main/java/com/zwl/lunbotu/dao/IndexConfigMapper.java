package com.zwl.lunbotu.dao;

import com.zwl.lunbotu.entity.IndexConfig;
import com.zwl.lunbotu.util.PageQueryUtil;

import java.util.List;

public interface IndexConfigMapper {

    //后台分页
    List<IndexConfig> findIndexConfigList(PageQueryUtil pageUtil);
    //得到数据的总行数
    int getTotalIndexConfigs(PageQueryUtil pageUtil);

    //将商品信息插入数据库
    int insertSelective(IndexConfig record);

    //删除首页配置数据
    int deleteBatch(Long[] ids);

    IndexConfig selectByPrimaryKey(Long configId);

    int updateByPrimaryKeySelective(IndexConfig indexConfig);

    List<IndexConfig> findIndexConfigsByTypeAndNum(int configType, int number);
}

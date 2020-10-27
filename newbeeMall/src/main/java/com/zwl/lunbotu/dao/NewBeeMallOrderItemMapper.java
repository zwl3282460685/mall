package com.zwl.lunbotu.dao;

import com.zwl.lunbotu.entity.NewBeeMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewBeeMallOrderItemMapper {

    int insertBatch(@Param("orderItems") List<NewBeeMallOrderItem> orderItems);

    //根据订单id获取订单项列表
    List<NewBeeMallOrderItem> selectByOrderId(Long ordersId);


    List<NewBeeMallOrderItem> selectByOrderIds(@Param("orderIds")List<Long> orderIds);
}

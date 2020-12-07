package com.zwl.mall.dao;

import com.zwl.mall.entity.NewBeeMallOrder;
import com.zwl.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewBeeMallOrderMapper {

    int insertSelective(NewBeeMallOrder newBeeMallOrder);

    NewBeeMallOrder selectByOrderNo(String orderNo);

    int getTotalNewBeeMallOrders(PageQueryUtil pageUtil);

    List<NewBeeMallOrder> findNewBeeMallOrderList(PageQueryUtil pageUtil);

    int closeOrder(@Param("orderIds")List<Long> orderIds, @Param("orderStatus")int orderStatus);

    int updateByPrimaryKeySelective(NewBeeMallOrder record);

    NewBeeMallOrder selectByPrimaryKey(Long orderId);

    int checkDone(@Param("orderIds")List<Long> orderIds);

    List<NewBeeMallOrder> selectByPrimaryKeys(@Param("orderIds")List<Long> orderIds);

    int checkOut(@Param("orderIds")List<Long> orderIds);
}

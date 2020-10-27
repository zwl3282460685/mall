package com.zwl.lunbotu.service;

import com.zwl.lunbotu.controller.vo.NewBeeMallOrderDetailVO;
import com.zwl.lunbotu.controller.vo.NewBeeMallOrderItemVO;
import com.zwl.lunbotu.controller.vo.NewBeeMallShoppingCartItemVO;
import com.zwl.lunbotu.controller.vo.NewBeeMallUserVO;
import com.zwl.lunbotu.entity.NewBeeMallOrder;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.PageResult;

import java.util.List;

public interface NewBeeMallOrderService {

    //保存订单
    String saveOrder(NewBeeMallUserVO user, List<NewBeeMallShoppingCartItemVO> myShoppingCartItems);

    //根据订单号得到订单详细信息
    NewBeeMallOrderDetailVO getOrderDetailByOrderNo(String orderNo);

    //我的订单列表
    PageResult getMyOrders(PageQueryUtil pageUtil);

    //手动取消订单
    String cancelOrder(String orderNo, Long userId);

    //确认收货
    String finishOrder(String orderNo, Long userId);

    //获取订单详情
    NewBeeMallOrder getNewBeeMallOrderByOrderNo(String orderNo);

    //付款成功
    String paySuccess(String orderNo, int payType);

    //后台分页
    PageResult getNewBeeMallOrdersPage(PageQueryUtil pageUtil);

    //更新订单信息
    String updateOrderInfo(NewBeeMallOrder newBeeMallOrder);

    //得到订单列表
    List<NewBeeMallOrderItemVO> getOrderItems(Long id);

    //配货
    String checkDone(Long[] ids);

    //出库
    String checkOut(Long[] ids);

    //关闭订单
    String closeOrder(Long[] ids);
}

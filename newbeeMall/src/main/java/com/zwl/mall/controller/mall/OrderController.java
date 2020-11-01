package com.zwl.mall.controller.mall;

import com.zwl.mall.common.Constants;
import com.zwl.mall.common.NewBeeMallException;
import com.zwl.mall.common.ServiceResultEnum;
import com.zwl.mall.controller.vo.NewBeeMallOrderDetailVO;
import com.zwl.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import com.zwl.mall.controller.vo.NewBeeMallUserVO;
import com.zwl.mall.entity.NewBeeMallOrder;
import com.zwl.mall.service.NewBeeMallOrderService;
import com.zwl.mall.service.NewBeeMallShoppingCartService;
import com.zwl.mall.util.PageQueryUtil;
import com.zwl.mall.util.Result;
import com.zwl.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Resource
    private NewBeeMallShoppingCartService newBeeMallShoppingCartService;

    @Resource
    private NewBeeMallOrderService newBeeMallOrderService;

    //生成订单
    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession httpSession){
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = newBeeMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if(StringUtils.isEmpty(user.getAddress().trim())){
            //无收货地址
            NewBeeMallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if(CollectionUtils.isEmpty(myShoppingCartItems)){
            NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }

        //保存订单并返回订单号
        String saveOrderResult = newBeeMallOrderService.saveOrder(user, myShoppingCartItems);
        return "redirect:/orders/" + saveOrderResult;
    }

    //根据订单id得到订单详情
    @GetMapping("/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo")String orderNo, HttpSession httpSession){
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        NewBeeMallOrderDetailVO orderDetailVO = newBeeMallOrderService.getOrderDetailByOrderNo(orderNo);
        if(orderDetailVO == null){
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }

    //得到订单列表
    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession){
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if(StringUtils.isEmpty(params.get("page"))){
            params.put("page",1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);

        //封装我的订单数据
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", newBeeMallOrderService.getMyOrders(pageQueryUtil));
        request.setAttribute("path", "orders");
        return "mall/my-orders";
    }

    //取消订单
    @PutMapping("/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession){
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancleOrderResult = newBeeMallOrderService.cancelOrder(orderNo, user.getUserId());
        if(ServiceResultEnum.SUCCESS.getResult().equals(cancleOrderResult)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(cancleOrderResult);
        }
    }

    //确认收货
    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession session){
        NewBeeMallUserVO user = (NewBeeMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = newBeeMallOrderService.finishOrder(orderNo, user.getUserId());
        if(ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    //选择支付方式
    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession){
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        NewBeeMallOrder newBeeMallOrder = newBeeMallOrderService.getNewBeeMallOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", newBeeMallOrder.getTotalPrice());
        return "mall/pay-select";
    }

    //支付界面
    @GetMapping("/payPage")
    public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession session,
                           @RequestParam("payType") int payType){
        NewBeeMallUserVO user = (NewBeeMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        NewBeeMallOrder newBeeMallOrder = newBeeMallOrderService.getNewBeeMallOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", newBeeMallOrder.getTotalPrice());
        if(payType == 1){
            return "mall/alipay";
        }else{
            return "mall/wxpay";
        }
    }

    @GetMapping("/paySuccess")
    @ResponseBody
    public Result paySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType){
        String payResult = newBeeMallOrderService.paySuccess(orderNo, payType);
        if(ServiceResultEnum.SUCCESS.getResult().equals(payResult)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(payResult);
        }
    }
}

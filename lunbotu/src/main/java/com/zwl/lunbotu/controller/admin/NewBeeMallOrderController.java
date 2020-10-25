package com.zwl.lunbotu.controller.admin;

import com.zwl.lunbotu.common.ServiceResultEnum;
import com.zwl.lunbotu.controller.vo.NewBeeMallOrderItemVO;
import com.zwl.lunbotu.entity.NewBeeMallOrder;
import com.zwl.lunbotu.service.NewBeeMallOrderService;
import com.zwl.lunbotu.service.NewBeeMallUserService;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.Result;
import com.zwl.lunbotu.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class NewBeeMallOrderController {
    @Resource
    private NewBeeMallOrderService newBeeMallOrderService;

    @GetMapping("/orders")
    public String ordersPage(HttpServletRequest request){
        request.setAttribute("path", "orders");
        return "admin/newbee_mall_order";
    }

    //列表
    @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallOrderService.getNewBeeMallOrdersPage(pageUtil));
    }

    //修改
    @RequestMapping(value = "/orders/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody NewBeeMallOrder newBeeMallOrder){
        if (Objects.isNull(newBeeMallOrder.getTotalPrice())
                || Objects.isNull(newBeeMallOrder.getOrderId())
                || newBeeMallOrder.getOrderId() < 1
                || newBeeMallOrder.getTotalPrice() < 1
                || StringUtils.isEmpty(newBeeMallOrder.getUserAddress())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallOrderService.updateOrderInfo(newBeeMallOrder);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult(result);
        }
    }

    //详情
    @RequestMapping(value = "/order-items/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        List<NewBeeMallOrderItemVO> orderItems = newBeeMallOrderService.getOrderItems(id);
        if (!CollectionUtils.isEmpty(orderItems)) {
            return ResultGenerator.genSuccessResult(orderItems);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    //配货
    @RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids) {
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常!");
        }
        String result = newBeeMallOrderService.checkDone(ids);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    //出库
    @RequestMapping(value="/orders/checkOut", method = RequestMethod.POST)
    @ResponseBody
    public Result checkOut(@RequestBody Long[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallOrderService.checkOut(ids);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    //关闭订单
    @RequestMapping(value="/orders/close", method = RequestMethod.POST)
    @ResponseBody
    public Result closeOrder(@RequestBody Long[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallOrderService.closeOrder(ids);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }
}

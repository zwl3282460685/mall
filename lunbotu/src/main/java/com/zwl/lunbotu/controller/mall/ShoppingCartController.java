package com.zwl.lunbotu.controller.mall;

import com.zwl.lunbotu.common.Constants;
import com.zwl.lunbotu.common.ServiceResultEnum;
import com.zwl.lunbotu.controller.vo.NewBeeMallShoppingCartItemVO;
import com.zwl.lunbotu.controller.vo.NewBeeMallUserVO;
import com.zwl.lunbotu.entity.NewBeeMallShoppingCartItem;
import com.zwl.lunbotu.service.NewBeeMallShoppingCartService;
import com.zwl.lunbotu.util.Result;
import com.zwl.lunbotu.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    NewBeeMallShoppingCartService newBeeMallShoppingCartService;

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveNewBeeMallShoppingCartItem(@RequestBody NewBeeMallShoppingCartItem newBeeMallShoppingCartItem,
                                                 HttpSession httpSession) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        //System.out.println(user.toString());
        newBeeMallShoppingCartItem.setUserId(user.getUserId());
        String saveResult = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newBeeMallShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request, HttpSession httpSession){
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        System.out.println(user.toString());
        int itemsTotal = 0;
        int priceTotal = 0;
        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = newBeeMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if(!CollectionUtils.isEmpty(myShoppingCartItems)){
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(NewBeeMallShoppingCartItemVO::getGoodsCount).sum();
            if(itemsTotal < 1){
                return "error/error_5xx";
            }

            //总价
            for(NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO : myShoppingCartItems){
                priceTotal += newBeeMallShoppingCartItemVO.getGoodsCount() * newBeeMallShoppingCartItemVO.getSellingPrice();
            }
            if(priceTotal < 1){
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateNewBeeMallShoppingCartItem(@RequestBody NewBeeMallShoppingCartItem newBeeMallShoppingCartItem,
                                                   HttpSession session) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        newBeeMallShoppingCartItem.setUserId(user.getUserId());
        String updateResult = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newBeeMallShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
    @ResponseBody
    public Result updateNewBeeMallShoppingCartItem(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId,
                                                   HttpSession httpSession){
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = newBeeMallShoppingCartService.deleteById(newBeeMallShoppingCartItemId);

        if(deleteResult){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession){
        int priceTotal = 0;
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = newBeeMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if(CollectionUtils.isEmpty(myShoppingCartItems)){
            return "/shop-cart";
        }else{
            for(NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO : myShoppingCartItems){
                priceTotal += newBeeMallShoppingCartItemVO.getGoodsCount() * newBeeMallShoppingCartItemVO.getSellingPrice();
            }
            if(priceTotal < 1){
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}

package com.zwl.lunbotu.controller.admin;

import com.zwl.lunbotu.common.ServiceResultEnum;
import com.zwl.lunbotu.entity.Carousel;
import com.zwl.lunbotu.service.NewBeeMallCarouselService;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.Result;
import com.zwl.lunbotu.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class NewBeeMallCarouselController {

    @Resource
    NewBeeMallCarouselService newBeeMallCarouselService;


    @GetMapping("/carousels")
    public String carouselPage(HttpServletRequest request){
        request.setAttribute("path", "newbee_mall_carousel");
        return "admin/newbee_mall_carousel";
    }

    /**
     * 获取轮播图列表
     */
    @GetMapping("/carousels/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
        if(StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))){
            return ResultGenerator.genFailResult("参数异常");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallCarouselService.getCarouselPage(pageQueryUtil));
    }

    /**
     * 添加轮播图
     */
    @RequestMapping(value = "/carousels/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Carousel carousel){
        if(StringUtils.isEmpty(carousel.getCarouselUrl()) || Objects.isNull(carousel.getCarouselRank())){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallCarouselService.savaCarousel(carousel);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 删除轮播图
     */
    @RequestMapping(value = "/carousels/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(newBeeMallCarouselService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败！");
        }
    }

    /**
     *获取轮播图信息
     */
    @GetMapping("/carousels/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id){
         Carousel carousel = newBeeMallCarouselService.getCarouselById(id);
         if(carousel == null){
             return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
         }
         return ResultGenerator.genSuccessResult(carousel);
    }


    /**
     * 更新轮播图
     * @param carousel
     * @return
     */
    @PostMapping("/carousels/update")
    @ResponseBody
    public Result update(@RequestBody Carousel carousel){
        if(Objects.isNull(carousel.getCarouselId())
                || StringUtils.isEmpty(carousel.getCarouselUrl())
                || Objects.isNull(carousel.getCarouselRank())){
            return ResultGenerator.genFailResult("参数异常");
        }
        String result = newBeeMallCarouselService.updateCarousel(carousel);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genSuccessResult(result);
        }
    }
}

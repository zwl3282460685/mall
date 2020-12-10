package com.zwl.mall.controller.admin;

import com.zwl.mall.common.ServiceResultEnum;
import com.zwl.mall.entity.Carousel;
import com.zwl.mall.service.NewBeeMallCarouselService;
import com.zwl.mall.util.PageQueryUtil;
import com.zwl.mall.util.Result;
import com.zwl.mall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@Api("轮播图相关接口")
public class NewBeeMallCarouselController {

    @Resource
    NewBeeMallCarouselService newBeeMallCarouselService;


    @GetMapping("/carousels")
    public String carouselPage(HttpServletRequest request){
        request.setAttribute("path", "newbee_mall_carousel");
        return "admin/newbee_mall_carousel";
    }

    @GetMapping("/carousels/list")
    @ResponseBody
    @ApiOperation("轮播图分页列表接口")
    public Result list(@RequestParam Map<String, Object> params){
        if(StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))){
            return ResultGenerator.genFailResult("参数异常");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallCarouselService.getCarouselPage(pageQueryUtil));
    }


    @RequestMapping(value = "/carousels/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("添加轮播图接口")
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


    @RequestMapping(value = "/carousels/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("删除轮播图接口")
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

    @GetMapping("/carousels/info/{id}")
    @ResponseBody
    @ApiOperation("获取轮播图信息接口")
    public Result info(@PathVariable("id") Integer id){
         Carousel carousel = newBeeMallCarouselService.getCarouselById(id);
         if(carousel == null){
             return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
         }
         return ResultGenerator.genSuccessResult(carousel);
    }

    @PostMapping("/carousels/update")
    @ResponseBody
    @ApiOperation("更新轮播图信息接口")
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

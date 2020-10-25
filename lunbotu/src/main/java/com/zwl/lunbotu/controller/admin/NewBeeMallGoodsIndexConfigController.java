package com.zwl.lunbotu.controller.admin;

import com.zwl.lunbotu.common.IndexConfigTypeEnum;
import com.zwl.lunbotu.common.ServiceResultEnum;
import com.zwl.lunbotu.entity.IndexConfig;
import com.zwl.lunbotu.service.NewBeeMallIndexConfigService;
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
public class NewBeeMallGoodsIndexConfigController {

    @Resource
    NewBeeMallIndexConfigService newBeeMallIndexConfigService;

    @GetMapping("/indexConfigs")
    public String indexConfigsPage(HttpServletRequest request, @RequestParam("configType") int configType){
        IndexConfigTypeEnum indexConfigTypeEnum = IndexConfigTypeEnum.getIndexConfigTypeEnumByType(configType);
        if(indexConfigTypeEnum.equals(IndexConfigTypeEnum.DEFAULT)){
            return "error/error_5xx";
        }

        request.setAttribute("path", indexConfigTypeEnum.getName());
        request.setAttribute("configType", configType);
        return "admin/newbee_mall_index_config";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/indexConfigs/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallIndexConfigService.getConfigsPage(pageUtil));
    }

    //添加首页配置
    @RequestMapping(value = "/indexConfigs/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody IndexConfig indexConfig){
        if(Objects.isNull(indexConfig.getConfigType())
                || StringUtils.isEmpty(indexConfig.getConfigName())
                || Objects.isNull(indexConfig.getConfigRank())){
            return ResultGenerator.genFailResult("参数错误");
        }
        String result = newBeeMallIndexConfigService.saveIndexConfig(indexConfig);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    //删除首页配置
    @RequestMapping(value = "/indexConfigs/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常！");
        }
        if(newBeeMallIndexConfigService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    //更新首页配置
    @RequestMapping(value = "/indexConfigs/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody IndexConfig indexConfig){
        if (Objects.isNull(indexConfig.getConfigType())
                || Objects.isNull(indexConfig.getConfigId())
                || StringUtils.isEmpty(indexConfig.getConfigName())
                || Objects.isNull(indexConfig.getConfigRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallIndexConfigService.updateIndexConfig(indexConfig);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }
}

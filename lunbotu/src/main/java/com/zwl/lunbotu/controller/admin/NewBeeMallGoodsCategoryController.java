package com.zwl.lunbotu.controller.admin;

import com.zwl.lunbotu.common.NewBeeMallCategoryLevelEnum;
import com.zwl.lunbotu.common.ServiceResultEnum;
import com.zwl.lunbotu.entity.GoodsCategory;
import com.zwl.lunbotu.service.NewBeeMallCategoryService;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.Result;
import com.zwl.lunbotu.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class NewBeeMallGoodsCategoryController {

    @Autowired
    NewBeeMallCategoryService newBeeMallCategoryService;

    /**
     * 列表
     */
    @RequestMapping(value = "/categories/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallCategoryService.getCategorisPage(pageQueryUtil));
    }

    /**
     * 添加商品类目
     *
     * @param goodsCategory
     * @return
     */
    @RequestMapping(value = "/categories/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody GoodsCategory goodsCategory) {
        if (Objects.isNull(goodsCategory.getCategoryLevel())
                || StringUtils.isEmpty(goodsCategory.getCategoryName())
                || Objects.isNull(goodsCategory.getParentId())
                || Objects.isNull(goodsCategory.getCategoryRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallCategoryService.saveCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改商品类目
     */
    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody GoodsCategory goodsCategory) {
        if (Objects.isNull(goodsCategory.getCategoryId())
                || Objects.isNull(goodsCategory.getCategoryLevel())
                || StringUtils.isEmpty(goodsCategory.getCategoryName())
                || Objects.isNull(goodsCategory.getParentId())
                || Objects.isNull(goodsCategory.getCategoryRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallCategoryService.updateGoodsCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 删除类目信息
     */
    @RequestMapping(value = "/categories/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数错误");
        }
        if (newBeeMallCategoryService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    /**
     *分类管理页面
     */
    @GetMapping("/categories")
    public String categoriesPage(HttpServletRequest request,
                                 @RequestParam("categoryLevel") Byte categoryLevel,
                                 @RequestParam("parentId") Long parentId,
                                 @RequestParam("backParentId") Long backParentId) {
        if(categoryLevel == null || categoryLevel < 1 || categoryLevel > 3){
            return "error/error_5xx";
        }

        request.setAttribute("path", "newbee_mall_category");
        request.setAttribute("parentId", parentId);
        request.setAttribute("backParentId", backParentId);
        request.setAttribute("categoryLevel", categoryLevel);
        return "admin/newbee_mall_category";
    }

    @GetMapping("/coupling-test")
    public String couplingTest(HttpServletRequest request){
        request.setAttribute("path", "coupling-test");
        //查询所有的一级分类
        List<GoodsCategory> firstLevelCategories = newBeeMallCategoryService.
                                                   selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), NewBeeMallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if(!CollectionUtils.isEmpty(firstLevelCategories)){
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if(!CollectionUtils.isEmpty(secondLevelCategories)){
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                return "admin/coupling-test";
            }
        }
        return "error/error_5xx";
    }

    @RequestMapping(value="/categories/listForSelect")
    @ResponseBody
    public Result listForSelect(@RequestParam("categoryId") Long categoryId){
        if(categoryId == null || categoryId < 1){
            return ResultGenerator.genFailResult("缺少参数");
        }
        GoodsCategory goodsCategory = newBeeMallCategoryService.getGoodCategoryById(categoryId);
        //既不是一级分类也不是二级分类则为不返回数据
        if(goodsCategory == null || goodsCategory.getCategoryLevel() == NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel()){
            return ResultGenerator.genFailResult("参数错误");
        }
        Map categoryResult = new HashMap();
        //如果是一级分类则返回当前一级分类下的所有二级分类，以及二级分类列表中第一条数据下的所有三级分类列表
        if(goodsCategory.getCategoryLevel() == NewBeeMallCategoryLevelEnum.LEVEL_ONE.getLevel()){
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if(!CollectionUtils.isEmpty(secondLevelCategories)){
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                categoryResult.put("secondLevelCategories", secondLevelCategories);
                categoryResult.put("thirdLevelCategories", thirdLevelCategories);
            }
        }
        if(goodsCategory.getCategoryLevel() == NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel()){
            //如果是二级分类则返回当前分类下的所有三级分类列表
            List<GoodsCategory> thirdLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
            categoryResult.put("thirdLevelCategories", thirdLevelCategories);
        }
        return ResultGenerator.genSuccessResult(categoryResult);
    }
}

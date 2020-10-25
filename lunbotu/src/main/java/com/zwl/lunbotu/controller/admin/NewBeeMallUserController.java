package com.zwl.lunbotu.controller.admin;

import com.zwl.lunbotu.service.NewBeeMallUserService;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.Result;
import com.zwl.lunbotu.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class NewBeeMallUserController {

    @Resource
    private NewBeeMallUserService newBeeMallUserService;

    //会员管理界面
    @GetMapping("/users")
    public String usersPage(HttpServletRequest request) {
        request.setAttribute("path", "users");
        return "admin/newbee_mall_user";
    }

    /**
     * 会员用户列表
     */
    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallUserService.getNewBeeMallUsersPage(pageUtil));
    }

    //用户禁用与解除禁用(0-未锁定 1-已锁定)
    @RequestMapping(value="/users/lock/{lockStatus}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids, @PathVariable("lockStatus") int lockStatus){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常！");
        }
        if(lockStatus != 0 && lockStatus != 1){
            return ResultGenerator.genFailResult("操作非法！");
        }
        if(newBeeMallUserService.lockUsers(ids, lockStatus)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("禁用失败");
        }
    }

}
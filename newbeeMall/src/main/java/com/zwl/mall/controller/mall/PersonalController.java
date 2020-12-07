package com.zwl.mall.controller.mall;

import com.zwl.mall.common.Constants;
import com.zwl.mall.common.ServiceResultEnum;
import com.zwl.mall.controller.vo.NewBeeMallUserVO;
import com.zwl.mall.entity.MallUser;
import com.zwl.mall.service.NewBeeMallUserService;
import com.zwl.mall.util.MD5Util;
import com.zwl.mall.util.Result;
import com.zwl.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PersonalController {

    @Resource
    private NewBeeMallUserService newBeeMallUserService;

    @GetMapping({"/login", "login.html"})
    public String login(){
        return "mall/login";
    }

    @GetMapping({"/register", "register.html"})
    public String register(){
        return "mall/register";
    }

//    @GetMapping("/personal/addresses")
//    public String addressesPage() {
//        return "mall/addresses";
//    }

    //登陆页面跳转
    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("loginName") String loginName,
                        @RequestParam("verifyCode") String verifyCode,
                        @RequestParam("password") String password,
                        HttpSession httpSession){
        if(StringUtils.isEmpty(loginName)){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if(StringUtils.isEmpty(verifyCode)){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
        if(StringUtils.isEmpty(password)){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        String kaptchaCode = httpSession.getAttribute(Constants.MALL_VERIFY_CODE_KEY) + "";
        if(StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        String loginResult = newBeeMallUserService.login(loginName, MD5Util.MD5Encode(password, "UTF-8"), httpSession);

        //登录成功
        if(ServiceResultEnum.SUCCESS.getResult().equals(loginResult)){
            return ResultGenerator.genSuccessResult();
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);

    }

    //注册页面跳转
    @PostMapping("/register")
    @ResponseBody
    public Result registerPage(@RequestParam("loginName") String loginName,
                               @RequestParam("verifyCode") String verifyCode,
                               @RequestParam("password") String password,
                               HttpSession httpSession){
        if(StringUtils.isEmpty(loginName)){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if(StringUtils.isEmpty(password)){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        if(StringUtils.isEmpty(verifyCode)){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }

        String kaptchaCode = httpSession.getAttribute(Constants.MALL_VERIFY_CODE_KEY) + "";
        if(StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }

        String registerResult = newBeeMallUserService.register(loginName, password);
        //注册成功
        if(ServiceResultEnum.SUCCESS.getResult().equals(registerResult)){
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    //登出
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute(Constants.MALL_USER_SESSION_KEY);
        return "mall/login";
    }

    //更新用户信息
    @PostMapping("/personal/updateInfo")
    @ResponseBody
    public Result updateInfo(@RequestBody MallUser mallUser, HttpSession httpSession){
        NewBeeMallUserVO mallUserVO = newBeeMallUserService.updateUserInfo(mallUser, httpSession);
        if(mallUserVO == null){
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }else{
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        }
    }

    @GetMapping("/personal")
    public String personlPage(HttpServletRequest request, HttpSession httpSession){
        request.setAttribute("path", "personal");
        return "mall/personal";
    }
}

package com.zwl.mall.service;

import com.zwl.mall.controller.vo.NewBeeMallUserVO;
import com.zwl.mall.entity.MallUser;
import com.zwl.mall.util.PageQueryUtil;
import com.zwl.mall.util.PageResult;

import javax.servlet.http.HttpSession;

public interface NewBeeMallUserService {
    //用户注册
    String register(String loginName, String password);

    //登录
    String login(String loginName, String md5Encode, HttpSession httpSession);

    //用户信息修改并返回最新的用户信息
    NewBeeMallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession);

    //会员用户后台分页
    PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil);

    //禁用用户
    Boolean lockUsers(Integer[] ids, int lockStatus);
}

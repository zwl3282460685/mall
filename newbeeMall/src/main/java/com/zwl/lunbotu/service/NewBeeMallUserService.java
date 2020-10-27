package com.zwl.lunbotu.service;

import com.zwl.lunbotu.controller.vo.NewBeeMallUserVO;
import com.zwl.lunbotu.entity.MallUser;
import com.zwl.lunbotu.util.PageQueryUtil;
import com.zwl.lunbotu.util.PageResult;

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

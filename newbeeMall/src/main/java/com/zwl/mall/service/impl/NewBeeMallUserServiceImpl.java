package com.zwl.mall.service.impl;

import com.zwl.mall.common.Constants;
import com.zwl.mall.common.ServiceResultEnum;
import com.zwl.mall.controller.vo.NewBeeMallUserVO;
import com.zwl.mall.dao.MallUserMapper;
import com.zwl.mall.entity.MallUser;
import com.zwl.mall.service.NewBeeMallUserService;
import com.zwl.mall.util.BeanUtil;
import com.zwl.mall.util.MD5Util;
import com.zwl.mall.util.PageQueryUtil;
import com.zwl.mall.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class NewBeeMallUserServiceImpl implements NewBeeMallUserService {

    @Resource
    private MallUserMapper mallUserMapper;

    @Override
    public String register(String loginName, String password) {
        if(mallUserMapper.selectByLoginName(loginName) != null){
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if(mallUserMapper.insertSelective(registerUser) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String password, HttpSession httpSession) {
        MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, password);
        if(user != null && httpSession != null){
            if(user.getLockedFlag() == 1){
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if(user.getNickName() != null && user.getNickName().length() > 7){
                String tmpName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tmpName);
            }
            NewBeeMallUserVO newBeeMallUserVO = new NewBeeMallUserVO();
            BeanUtil.copyProperties(user, newBeeMallUserVO);
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, newBeeMallUserVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    //用户信息修改并返回最新的用户信息
    @Override
    public NewBeeMallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession) {
        MallUser user = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
        if(null != user){
            user.setNickName(mallUser.getNickName());
            user.setAddress(mallUser.getAddress());
            user.setIntroduceSign(mallUser.getIntroduceSign());
            if(mallUserMapper.updateByPrimaryKeySelective(user) > 0){
                NewBeeMallUserVO newBeeMallUserVO = new NewBeeMallUserVO();
                user = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
                BeanUtil.copyProperties(user, newBeeMallUserVO);
                httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, newBeeMallUserVO);
                return newBeeMallUserVO;
            }
        }
        return null;
    }

    //查询商城用户
    @Override
    public PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil) {
        List<MallUser> mallUsers = mallUserMapper.findMallUserList(pageUtil);
        int total = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    //用户禁用与解除禁用(0-未锁定 1-已锁定)
    @Override
    public Boolean lockUsers(Integer[] ids, int lockStatus) {
        if(ids.length < 1){
            return false;
        }
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }
}

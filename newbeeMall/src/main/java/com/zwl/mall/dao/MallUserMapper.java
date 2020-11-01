package com.zwl.mall.dao;


import com.zwl.mall.entity.MallUser;
import com.zwl.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallUserMapper {
    //根据登录名查找用户
    MallUser selectByLoginName(String loginName);

    //插入用户信息
    int insertSelective(MallUser registerUser);

    //根据用户名难和密码查询用户
    MallUser selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

    //根据用户id查找用户
    MallUser selectByPrimaryKey(Long userId);

    //根据用户id用户信息的更新
    int updateByPrimaryKeySelective(MallUser user);

    //得到商城用户列表
    List<MallUser> findMallUserList(PageQueryUtil pageUtil);

    //得到商城用户总数
    int getTotalMallUsers(PageQueryUtil pageUtil);

    //批量禁用用户(0-未锁定 1-已锁定)
    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);
}

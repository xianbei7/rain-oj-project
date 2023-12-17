package com.rain.oj.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rain.oj.model.dto.user.UserQueryRequest;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.vo.LoginUserVO;
import com.rain.oj.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userNumber    用户学号
     * @param userAccount   用户账户
     * @param userName      用户姓名
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return {@link Long} 新用户 id
     */
    Long userRegister(String userNumber, String userAccount, String userName, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      Http请求
     * @return {@link LoginUserVO}脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request Http请求
     * @return {@link User} 登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request Http请求
     * @return {@link User} 登录用户
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request Http请求
     * @return {@link boolean} 管理员返回 true
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user 用户
     * @return {@link boolean} 管理员返回 true
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request Http请求
     * @return {@link boolean} 注销成功返回 true
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param user 用户
     * @return {@link LoginUserVO} 用户vo
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户
     * @return {@link UserVO} 用户vo
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList 用户列表
     * @return {@link UserVO} 用户vo列表
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 用户查询请求
     * @return {@link LambdaQueryWrapper<User>} 查询条件
     */
    LambdaQueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

}

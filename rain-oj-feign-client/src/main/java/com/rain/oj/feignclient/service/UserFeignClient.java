package com.rain.oj.feignclient.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.constant.UserConstant;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.model.entity.User;
import com.rain.oj.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 用户服务
 */
@FeignClient(name = "user-service", path = "/api/user/feign")
public interface UserFeignClient {

    /**
     * 根据id 获取用户
     *
     * @param userId 用户id
     * @return {@link User} 用户
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") Long userId);

    /**
     * 根据条件查询用户
     *
     * @param lambdaQueryWrapper 查询条件
     * @return {@link List<User>} 用户列表
     */
    @PostMapping("/get/list")
    List<User> list(@RequestBody LambdaQueryWrapper<User> lambdaQueryWrapper);

    /**
     * 根据id集合查询用户
     *
     * @param ids id集合
     * @return {@link List<User>} 用户列表
     */
    @PostMapping("/get/ids")
    List<User> listByIds(@RequestBody Collection<Long> ids);

    /**
     * 获取当前登录用户
     *
     * @param request Http请求
     * @return {@link User} 登录用户
     */
    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request Http请求
     * @return {@link User} 登录用户
     */
    default User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        return currentUser;
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户
     * @return {@link UserVO} 用户vo
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 是否为管理员
     *
     * @param user 用户
     * @return {@link boolean} 管理员返回 true
     */
    default boolean isAdmin(User user) {
        return UserConstant.ADMIN_ROLE.equals(user.getUserRole());
    }
}

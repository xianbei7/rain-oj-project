package com.rain.oj.userservice.controller.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rain.oj.model.entity.User;
import com.rain.oj.feignclient.service.UserFeignClient;
import com.rain.oj.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 服务间调用-用户服务
 */
@RestController
@RequestMapping("/feign")
public class UserFeignController implements UserFeignClient {
    @Resource
    private UserService userService;

    /**
     * 根据id 获取用户
     *
     * @param userId 用户id
     * @return {@link User} 用户
     */
    @GetMapping("/get/id")
    @Override
    public User getById(@RequestParam("userId") Long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据条件查询用户
     *
     * @param lambdaQueryWrapper 查询条件
     * @return {@link List<User>} 用户列表
     */
    @PostMapping("/get/list")
    @Override
    public List<User> list(@RequestBody LambdaQueryWrapper<User> lambdaQueryWrapper) {
        return userService.list(lambdaQueryWrapper);
    }

    /**
     * 根据id集合查询用户
     *
     * @param ids id集合
     * @return {@link List<User>} 用户列表
     */
    @PostMapping("/get/ids")
    @Override
    public List<User> listByIds(@RequestBody Collection<Long> ids) {
        return userService.listByIds(ids);
    }
}

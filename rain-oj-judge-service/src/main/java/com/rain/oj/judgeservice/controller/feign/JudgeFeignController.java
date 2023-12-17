package com.rain.oj.judgeservice.controller.feign;

import com.rain.oj.feignclient.service.JudgeFeignClient;
import com.rain.oj.judgeservice.service.JudgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 服务间调用-题目服务
 */
@RestController
@RequestMapping("/feign")
public class JudgeFeignController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    /**
     * 根据id 获取用户
     *
     * @param questionSubmitId 题目提交id
     * @return {@link Boolean} 是否进行判题
     */
    @Override
    @GetMapping("/do")
    public Boolean doJudge(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}

package com.rain.oj.judgeservice.codesandbox;

import cn.hutool.json.JSONUtil;
import com.rain.oj.common.ErrorCode;
import com.rain.oj.common.constant.JudgeConstant;
import com.rain.oj.common.exception.BusinessException;
import com.rain.oj.model.judge.codesandbox.ExecuteCodeRequest;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 远程代码沙箱
 */
@Service
public class CodeSandboxTemplate {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public Boolean executeCode(ExecuteCodeRequest executeCodeRequest) {
        /*String url = "http://192.168.126.145:9400/code/sandbox/execute";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);*/
        try {
            String message = JSONUtil.toJsonStr(executeCodeRequest);
            rabbitTemplate.convertAndSend(JudgeConstant.CODE_SANDBOX_EXCHANGE, JudgeConstant.CODE_SANDBOX_SEND_ROUTING_KEY, message);
        } catch (AmqpException e) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "发送消息失败");
        }
        return true;
    }
}

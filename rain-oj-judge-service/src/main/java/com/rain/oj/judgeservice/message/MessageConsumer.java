package com.rain.oj.judgeservice.message;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.rain.oj.common.constant.JudgeConstant;
import com.rain.oj.judgeservice.codesandbox.CodeSandboxTemplate;
import com.rain.oj.judgeservice.service.JudgeService;
import com.rain.oj.model.judge.codesandbox.ExecuteCodeRequest;
import com.rain.oj.model.judge.codesandbox.ExecuteCodeResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class MessageConsumer {
    @Resource
    private JudgeService judgeService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private CodeSandboxTemplate codeSandboxTemplate;

    @SneakyThrows
    @RabbitListener(queues = JudgeConstant.CODE_SANDBOX_RECEIVE_QUEUE, ackMode = "MANUAL")
    public void receiveCodeSandboxMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(message, ExecuteCodeResponse.class);
        try {
            judgeService.judgeAndUpdate(executeCodeResponse);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("接收到执行响应，更新数据库异常{}", e.getMessage());
            // 重新加入队列进行处理
            channel.basicNack(deliveryTag, false, true);
        }
    }

    @SneakyThrows
    @RabbitListener(queues = JudgeConstant.DEAD_LETTER_SEND_QUEUE, ackMode = "MANUAL")
    public void receiveDeadLetter(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        ExecuteCodeRequest executeCodeRequest = JSONUtil.toBean(message, ExecuteCodeRequest.class);
        String executeId = executeCodeRequest.getExecuteId();
        Long times = stringRedisTemplate.opsForValue().increment(JudgeConstant.EXECUTE_RETRY_TIMES + executeId);
        if (times < JudgeConstant.MAX_EXECUTE_RETRY_TIMES) {
            codeSandboxTemplate.executeCode(executeCodeRequest);
        } else {
            judgeService.setJudgeFail(executeId);
        }
        channel.basicAck(deliveryTag, false);
    }
}

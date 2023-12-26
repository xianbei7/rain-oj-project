package com.rain.oj.judgeservice.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.rain.oj.common.constant.JudgeConstant.*;

@Slf4j
@Component
public class IntRabbitMq {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @PostConstruct
    public void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            // 创建交换机
            channel.exchangeDeclare(CODE_SANDBOX_EXCHANGE, EXCHANGE_TYPE);
            // 创建死信交换机
            channel.exchangeDeclare(DEAD_LETTER_EXCHANGE, EXCHANGE_TYPE);

            // 创建死信队列
            channel.queueDeclare(DEAD_LETTER_SEND_QUEUE, true, false, false, null);
            channel.queueBind(DEAD_LETTER_SEND_QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_SEND_ROUTING_KEY);

            // 指定绑定死信交换机参数
            Map<String, Object> args = new HashMap<>();
            args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
            args.put("x-dead-letter-routing-key", DEAD_LETTER_SEND_ROUTING_KEY);

            // 创建队列（发送执行代码沙箱请求）
            channel.queueDeclare(CODE_SANDBOX_SEND_QUEUE, true, false, false, args);
            channel.queueBind(CODE_SANDBOX_SEND_QUEUE, CODE_SANDBOX_EXCHANGE, CODE_SANDBOX_SEND_ROUTING_KEY);
            // 创建队列（接收执行代码沙箱响应）
            channel.queueDeclare(CODE_SANDBOX_RECEIVE_QUEUE, true, false, false, null);
            channel.queueBind(CODE_SANDBOX_RECEIVE_QUEUE, CODE_SANDBOX_EXCHANGE, CODE_SANDBOX_RECEIVE_ROUTING_KEY);
            log.info("消息队列启动成功");
        } catch (IOException | TimeoutException e) {
            log.error("消息队列启动失败", e);
        }
    }
}

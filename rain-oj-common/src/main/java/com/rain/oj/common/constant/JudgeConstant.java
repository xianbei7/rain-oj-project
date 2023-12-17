package com.rain.oj.common.constant;

/**
 * 题目常量
 */
public interface JudgeConstant {
    Integer MAX_EXECUTE_RETRY_TIMES = 3;
    String EXECUTE_RETRY_TIMES = "execute:times:";
    String JUDGE_CONTEXT_KEY = "judge:context:";
    Integer JUDGE_CONTEXT_EXPIRE_TIME = 5;
    String EXCHANGE_TYPE = "direct";

    String CODE_SANDBOX_EXCHANGE = "code_sandbox_exchange";
    String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";
    String CODE_SANDBOX_SEND_ROUTING_KEY = "send";
    String CODE_SANDBOX_RECEIVE_ROUTING_KEY = "receive";
    String DEAD_LETTER_SEND_ROUTING_KEY = "send";
    String CODE_SANDBOX_SEND_QUEUE = "code_sandbox_send_queue";
    String CODE_SANDBOX_RECEIVE_QUEUE = "code_sandbox_receive_queue";
    String DEAD_LETTER_SEND_QUEUE = "dead_letter_send_queue";

}

package com.mingdong.mis.service;

import com.mingdong.core.constant.MessageType;

public interface ClientMessageService
{
    /**
     * 给客户发送消息通知
     *
     * @param messageTypeEnum
     * @param clientId        客户id
     * @param content         内容
     * @param isSendSMS       是否发送短信
     * @param SMSContent      短信内容
     */
    void sendMessage(MessageType messageTypeEnum, Long clientId, String content, boolean isSendSMS, String SMSContent);
}

package com.mingdong.mis.service;

public interface ClientMessageService
{
    /**
     * 给客户发送消息通知
     *
     * @param typeId     消息类型
     * @param clientId   客户id
     * @param content    内容
     * @param isSendSMS  是否发送短信
     * @param SMSContent 短信内容
     */
    void sendMessage(int typeId, Long clientId, String content, boolean isSendSMS, String SMSContent);
}

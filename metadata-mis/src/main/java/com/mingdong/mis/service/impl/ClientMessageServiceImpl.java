package com.mingdong.mis.service.impl;

import com.mingdong.core.constant.MessageType;
import com.mingdong.mis.domain.entity.ClientMessage;
import com.mingdong.mis.domain.mapper.ClientMessageMapper;
import com.mingdong.mis.service.ClientMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ClientMessageServiceImpl implements ClientMessageService
{
    @Resource
    private ClientMessageMapper clientMessageMapper;

    @Override
    @Transactional
    public void sendMessage(MessageType messageTypeEnum, Long clientId, String content, boolean isSendSMS,
            String SMSContent)
    {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setClientId(clientId);
        clientMessage.setContent(content);
        clientMessage.setType(1);
        Date date = new Date();
        clientMessage.setCreateTime(date);
        clientMessage.setUpdateTime(date);
        clientMessageMapper.add(clientMessage);
    }
}

package com.mingdong.mis.service.impl;

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
    public void sendMessage(int typeId, Long clientId, String content, boolean isSendSMS, String SMSContent)
    {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setClientId(clientId);
        clientMessage.setContent(content);
        clientMessage.setType(typeId);
        Date date = new Date();
        clientMessage.setCreateTime(date);
        clientMessage.setUpdateTime(date);
        clientMessageMapper.add(clientMessage);
        if(isSendSMS){
            //TODO 预留短信发送

        }
    }
}

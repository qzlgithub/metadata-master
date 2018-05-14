package com.mingdong.mis.service.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.mis.domain.entity.ClientContact;
import com.mingdong.mis.domain.entity.ClientMessage;
import com.mingdong.mis.domain.mapper.ClientContactMapper;
import com.mingdong.mis.domain.mapper.ClientMessageMapper;
import com.mingdong.mis.service.ClientMessageService;
import com.mingdong.mis.service.SMSService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ClientMessageServiceImpl implements ClientMessageService
{
    @Resource
    private ClientMessageMapper clientMessageMapper;
    @Resource
    private ClientContactMapper clientContactMapper;
    @Resource
    private SMSService smsService;

    @Override
    @Transactional
    public void sendMessage(int typeId, Long clientId, String content, boolean isSendSMS, String SMSContent,
            Integer smsType)
    {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setClientId(clientId);
        clientMessage.setContent(content);
        clientMessage.setType(typeId);
        Date date = new Date();
        clientMessage.setCreateTime(date);
        clientMessage.setUpdateTime(date);
        clientMessageMapper.add(clientMessage);
        if(isSendSMS)
        {
            List<ClientContact> clientContactList = clientContactMapper.getListByClient(clientId);
            if(!CollectionUtils.isEmpty(clientContactList))
            {
                ClientContact clientContact = clientContactList.get(0);
                try
                {
                    smsService.sendSMS(smsType, SMSContent, clientContact.getPhone());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

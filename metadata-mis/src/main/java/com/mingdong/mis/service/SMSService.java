package com.mingdong.mis.service;

import java.io.IOException;

public interface SMSService
{
    void sendSMS(Integer type, String content, String phone) throws IOException;
}

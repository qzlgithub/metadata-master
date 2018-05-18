package com.mingdong.mis.service;

import com.mingdong.mis.model.MDResp;

import java.util.List;

public interface DetectService
{
    boolean isDetectionTokenValid(String token);

    void getBlacklistInfo(List<String> phones, MDResp resp);

    void getOverdueInfo(List<String> phones, MDResp resp);

    void getMultiRegisterInfo(List<String> phones, MDResp resp);

    void getCreditableInfo(List<String> phones, MDResp resp);

    void getFavourableInfo(List<String> phones, MDResp resp);

    void getRejecteeInfo(List<String> phones, MDResp resp);

    void getHologramInfo(List<String> phones, MDResp resp) throws Exception;
}

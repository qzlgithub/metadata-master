package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.CorpTradeInfo;

import java.util.List;

public interface CorpTradeInfoMapper
{
    List<CorpTradeInfo> getCorpRechargeTrade(Long corpId);

    List<CorpTradeInfo> getCorpSpendTrade(Long corpId);
}

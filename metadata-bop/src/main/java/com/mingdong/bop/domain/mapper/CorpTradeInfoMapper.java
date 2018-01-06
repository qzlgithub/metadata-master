package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.CorpTradeInfo;

import java.util.List;

public interface CorpTradeInfoMapper
{
    List<CorpTradeInfo> getCorpRechargeTrade(Long corpId);

    List<CorpTradeInfo> getCorpSpendTrade(Long corpId);
}

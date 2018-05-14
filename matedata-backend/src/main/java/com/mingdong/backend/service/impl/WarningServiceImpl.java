package com.mingdong.backend.service.impl;

import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.domain.entity.WarningManage;
import com.mingdong.backend.domain.entity.WarningManageDetail;
import com.mingdong.backend.domain.entity.WarningOut;
import com.mingdong.backend.domain.entity.WarningPacify;
import com.mingdong.backend.domain.entity.WarningPacifyProduct;
import com.mingdong.backend.domain.entity.WarningSetting;
import com.mingdong.backend.domain.mapper.WarningManageDetailMapper;
import com.mingdong.backend.domain.mapper.WarningManageMapper;
import com.mingdong.backend.domain.mapper.WarningOutMapper;
import com.mingdong.backend.domain.mapper.WarningPacifyMapper;
import com.mingdong.backend.domain.mapper.WarningPacifyProductMapper;
import com.mingdong.backend.domain.mapper.WarningSettingMapper;
import com.mingdong.backend.model.Traffic;
import com.mingdong.backend.service.WarningService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.QueryStatus;
import com.mingdong.core.constant.SMSType;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.constant.WarningCode;
import com.mingdong.core.constant.WarningLevel;
import com.mingdong.core.model.dto.request.SMSReqDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.ManagerInfoResDTO;
import com.mingdong.core.model.dto.response.WarningManageDetailResDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.ManagerRpcService;
import com.mingdong.core.service.SystemRpcService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class WarningServiceImpl implements WarningService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private WarningSettingMapper warningSettingMapper;
    @Resource
    private WarningOutMapper warningOutMapper;
    @Resource
    private WarningManageMapper warningManageMapper;
    @Resource
    private WarningManageDetailMapper warningManageDetailMapper;
    @Resource
    private WarningPacifyMapper warningPacifyMapper;
    @Resource
    private WarningPacifyProductMapper warningPacifyProductMapper;
    @Resource
    private ClientRpcService clientRpcService;
    @Resource
    private SystemRpcService systemRpcService;
    @Resource
    private ManagerRpcService managerRpcService;

    @Override
    @Transactional
    public void verifyWarning(Traffic traffic, QueryStatus queryStatus)
    {
        WarningManageDetailResDTO warningManageDetailResDTO = new WarningManageDetailResDTO();
        Date happenTime = new Date(traffic.getTimestamp());
        warningManageDetailResDTO.setTime(happenTime);
        warningManageDetailResDTO.setProductId(traffic.getProductId());
        warningManageDetailResDTO.setClientId(traffic.getClientId());
        warningManageDetailResDTO.setClientUserId(traffic.getUserId());
        warningManageDetailResDTO.setRequestIp(traffic.getHost());
        warningManageDetailResDTO.setErrorType(queryStatus.getCode());
        Long productErrorCount = redisDao.cacheWarningProduct(warningManageDetailResDTO);
        Long clientErrorCount = redisDao.cacheWarningClient(warningManageDetailResDTO);
        WarningSetting productAnomaly = redisDao.getWarningSetting(WarningCode.PRODUCT_ANOMALY.getCode());
        WarningSetting clientFailure = redisDao.getWarningSetting(WarningCode.CLIENT_FAILURE.getCode());
        if(productAnomaly == null)
        {
            productAnomaly = warningSettingMapper.findByCode(WarningCode.PRODUCT_ANOMALY.getCode());
            redisDao.cacheWarningSetting(productAnomaly);
        }
        if(clientFailure == null)
        {
            clientFailure = warningSettingMapper.findByCode(WarningCode.CLIENT_FAILURE.getCode());
            redisDao.cacheWarningSetting(clientFailure);
        }
        warningLimit(traffic, happenTime, productErrorCount, productAnomaly, WarningCode.PRODUCT_ANOMALY);
        warningLimit(traffic, happenTime, clientErrorCount, clientFailure, WarningCode.CLIENT_FAILURE);
    }

    private void warningLimit(Traffic traffic, Date happenTime, Long count, WarningSetting warningSetting,
            WarningCode warningCode)
    {
        if(TrueOrFalse.FALSE.equals(warningSetting.getEnabled()))
        {
            return;
        }
        if(warningSetting.getGeneralLimit().equals(count.intValue()))
        {
            //一般
            saveWarningOut(happenTime, count, WarningLevel.GENERAL.getId(), warningCode.getCode(),
                    traffic.getProductId(), null);
        }
        else if(warningSetting.getSeverityLimit().equals(count.intValue()))
        {
            //严重
            saveWarningOut(happenTime, count, WarningLevel.SEVERITY.getId(), warningCode.getCode(),
                    traffic.getProductId(), null);
        }
        else if(warningSetting.getWarningLimit().equals(count.intValue()))
        {
            //警告
            saveWarning(warningSetting,traffic, warningCode);
        }
    }

    private void saveWarning(WarningSetting warningSetting,Traffic traffic, WarningCode warningCode)
    {
        Date date = new Date();
        WarningManage warningManage = new WarningManage();
        warningManage.setCreateTime(date);
        warningManage.setUpdateTime(date);
        warningManage.setWarningCode(warningCode.getCode());
        warningManage.setDispose(TrueOrFalse.FALSE);
        if(warningCode == WarningCode.PRODUCT_ANOMALY)
        {
            warningManage.setProductId(traffic.getProductId());
        }
        else if(warningCode == WarningCode.CLIENT_FAILURE)
        {
            warningManage.setClientId(traffic.getClientId());
            warningManage.setProductId(traffic.getProductId());
        }
        warningManageMapper.add(warningManage);
        WarningManageDetail warningManageDetailTemp;
        List<WarningManageDetailResDTO> list = null;
        if(warningCode == WarningCode.PRODUCT_ANOMALY)
        {
            list = redisDao.getWarningManageDetailListByProductId(traffic.getProductId());
        }
        else if(warningCode == WarningCode.CLIENT_FAILURE)
        {
            list = redisDao.getWarningManageDetailListByClientIdAndProductId(traffic.getClientId(),
                    traffic.getProductId());
        }
        List<WarningManageDetail> detailAddList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list))
        {
            for(WarningManageDetailResDTO item : list)
            {
                warningManageDetailTemp = new WarningManageDetail();
                warningManageDetailTemp.setCreateTime(date);
                warningManageDetailTemp.setUpdateTime(date);
                warningManageDetailTemp.setClientId(item.getClientId());
                warningManageDetailTemp.setClientUserId(item.getClientUserId());
                warningManageDetailTemp.setErrorType(item.getErrorType());
                warningManageDetailTemp.setManageId(warningManage.getId());
                warningManageDetailTemp.setProductId(item.getProductId());
                warningManageDetailTemp.setRequestIp(item.getRequestIp());
                warningManageDetailTemp.setTime(item.getTime());
                detailAddList.add(warningManageDetailTemp);
            }
            warningManageDetailMapper.addAll(detailAddList);
            List<WarningPacifyProduct> pacifyProductAddList = new ArrayList<>();
            Set<String> productClientSet = new HashSet<>();
            Set<Long> clientIds = new HashSet<>();
            for(WarningManageDetail item : detailAddList)
            {
                productClientSet.add(item.getProductId() + "," + item.getClientId());
                clientIds.add(item.getClientId());
            }
            List<ClientInfoResDTO> clientListByIds = clientRpcService.getClientListByIds(new ArrayList<>(clientIds));
            Map<Long, List<ClientInfoResDTO>> managerClientMap = new HashMap<>();
            List<ClientInfoResDTO> clientListTemp;
            for(ClientInfoResDTO item : clientListByIds)
            {
                if(item.getManagerInfoResDTO() == null || item.getManagerInfoResDTO().getManagerId() == null)
                {
                    continue;
                }
                clientListTemp = managerClientMap.computeIfAbsent(item.getManagerInfoResDTO().getManagerId(),
                        k -> new ArrayList<>());
                clientListTemp.add(item);
            }
            List<ManagerInfoResDTO> accountInfoListByAlarm = managerRpcService.getAccountInfoListByAlarm(
                    TrueOrFalse.TRUE, TrueOrFalse.TRUE);
            WarningPacify warningPacifyTemp;
            WarningPacifyProduct warningPacifyProductTemp;
            for(String item : productClientSet)
            {
                String[] split = item.split(",");
                warningPacifyTemp = new WarningPacify();
                warningPacifyTemp.setCreateTime(date);
                warningPacifyTemp.setUpdateTime(date);
                warningPacifyTemp.setDispose(TrueOrFalse.FALSE);
                warningPacifyTemp.setManageId(warningManage.getId());
                warningPacifyTemp.setClientId(Long.valueOf(split[1]));
                warningPacifyMapper.add(warningPacifyTemp);
                warningPacifyProductTemp = new WarningPacifyProduct();
                warningPacifyProductTemp.setCreateTime(date);
                warningPacifyProductTemp.setUpdateTime(date);
                warningPacifyProductTemp.setPacifyId(warningPacifyTemp.getId());
                warningPacifyProductTemp.setProductId(Long.valueOf(split[0]));
                pacifyProductAddList.add(warningPacifyProductTemp);
            }
            warningPacifyProductMapper.addAll(pacifyProductAddList);
            if(TrueOrFalse.TRUE.equals(warningSetting.getSend())){
                new Thread(() -> {
                    List<SMSReqDTO> smsList = new ArrayList<>();
                    if(!CollectionUtils.isEmpty(accountInfoListByAlarm))
                    {
                        for(ManagerInfoResDTO item : accountInfoListByAlarm)
                        {
                            SMSReqDTO smsReqDTO = new SMSReqDTO();
                            smsList.add(smsReqDTO);
                            smsReqDTO.setSmsType(SMSType.WARNING.getId());
                            smsReqDTO.setPhone(item.getPhone());
                            String content = "你好，" + item.getName() + "。于" + DateUtils.format(date,
                                    DateFormat.YYYY_MM_DD_HH_MM_SS) + "发生" + warningCode.getWarningType().getName() +
                                    warningCode.getName() + "警报，请及时处理！";
                            smsReqDTO.setContent(content);
                        }
                    }
                    managerClientMap.forEach((k, v) -> {
                        ManagerInfoResDTO managerInfoResDTO = v.get(0).getManagerInfoResDTO();
                        if(TrueOrFalse.TRUE.equals(managerInfoResDTO.getAlarm()))
                        {
                            SMSReqDTO smsReqDTO = new SMSReqDTO();
                            smsList.add(smsReqDTO);
                            smsReqDTO.setSmsType(SMSType.WARNING.getId());
                            smsReqDTO.setPhone(managerInfoResDTO.getPhone());
                            String content =
                                    "你好，" + managerInfoResDTO.getName() + "。因受到警报影响，共有" + v.size() + "位客户需要安抚，请注意。";
                            smsReqDTO.setContent(content);
                        }
                    });
                    systemRpcService.sendSMS(smsList);
                }).start();
            }
        }
    }

    private void saveWarningOut(Date happenTime, Long count, Integer level, String warningCode, Long productId,
            Long clientId)
    {
        Date date = new Date();
        WarningOut warningOut = new WarningOut();
        warningOut.setCreateTime(date);
        warningOut.setUpdateTime(date);
        warningOut.setLastTime(happenTime);
        warningOut.setCount(count);
        warningOut.setLevel(level);
        warningOut.setWarningCode(warningCode);
        warningOut.setProductId(productId);
        warningOut.setClientId(clientId);
        warningOutMapper.add(warningOut);
    }
}

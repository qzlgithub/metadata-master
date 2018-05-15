package com.mingdong.backend.service.rpc;

import com.github.pagehelper.PageHelper;
import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.domain.entity.WarningManage;
import com.mingdong.backend.domain.entity.WarningManageDetail;
import com.mingdong.backend.domain.entity.WarningManageInfo;
import com.mingdong.backend.domain.entity.WarningOut;
import com.mingdong.backend.domain.entity.WarningPacify;
import com.mingdong.backend.domain.entity.WarningPacifyInfo;
import com.mingdong.backend.domain.entity.WarningPacifyProduct;
import com.mingdong.backend.domain.entity.WarningSetting;
import com.mingdong.backend.domain.mapper.WarningManageDetailMapper;
import com.mingdong.backend.domain.mapper.WarningManageMapper;
import com.mingdong.backend.domain.mapper.WarningOutMapper;
import com.mingdong.backend.domain.mapper.WarningPacifyInfoMapper;
import com.mingdong.backend.domain.mapper.WarningPacifyMapper;
import com.mingdong.backend.domain.mapper.WarningPacifyProductMapper;
import com.mingdong.backend.domain.mapper.WarningSettingMapper;
import com.mingdong.backend.service.BackendWarningService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.WarningManageReqDTO;
import com.mingdong.core.model.dto.request.WarningPacifyReqDTO;
import com.mingdong.core.model.dto.request.WarningSettingReqDTO;
import com.mingdong.core.model.dto.response.ClientContactResDTO;
import com.mingdong.core.model.dto.response.ClientDetailResDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.ClientUserResDTO;
import com.mingdong.core.model.dto.response.ManagerInfoResDTO;
import com.mingdong.core.model.dto.response.ProductResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.UserInfoResDTO;
import com.mingdong.core.model.dto.response.WarningManageDetailResDTO;
import com.mingdong.core.model.dto.response.WarningManageResDTO;
import com.mingdong.core.model.dto.response.WarningOutResDTO;
import com.mingdong.core.model.dto.response.WarningPacifyInfoResDTO;
import com.mingdong.core.model.dto.response.WarningPacifyProductResDTO;
import com.mingdong.core.model.dto.response.WarningSettingResDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.ManagerRpcService;
import com.mingdong.core.service.ProductRpcService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BackendWarningServiceImpl implements BackendWarningService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private WarningSettingMapper warningSettingMapper;
    @Resource
    private WarningManageMapper warningManageMapper;
    @Resource
    private ProductRpcService productRpcService;
    @Resource
    private ClientRpcService clientRpcService;
    @Resource
    private WarningManageDetailMapper warningManageDetailMapper;
    @Resource
    private WarningOutMapper warningOutMapper;
    @Resource
    private ManagerRpcService managerRpcService;
    @Resource
    private WarningPacifyInfoMapper warningPacifyInfoMapper;
    @Resource
    private WarningPacifyMapper warningPacifyMapper;
    @Resource
    private WarningPacifyProductMapper warningPacifyProductMapper;

    @Override
    public WarningSettingResDTO getWarningSetting(Long id)
    {
        WarningSetting warningSetting = warningSettingMapper.findById(id);
        if(warningSetting == null)
        {
            return null;
        }
        WarningSettingResDTO warningSettingResDTO = new WarningSettingResDTO();
        warningSettingResDTO.setId(warningSetting.getId());
        warningSettingResDTO.setFileName(warningSetting.getFileName());
        warningSettingResDTO.setWarningLimit(warningSetting.getWarningLimit());
        warningSettingResDTO.setType(warningSetting.getType());
        warningSettingResDTO.setSeverityLimit(warningSetting.getSeverityLimit());
        warningSettingResDTO.setSend(warningSetting.getSend());
        warningSettingResDTO.setPlay(warningSetting.getPlay());
        warningSettingResDTO.setGeneralLimit(warningSetting.getGeneralLimit());
        warningSettingResDTO.setFilePath(warningSetting.getFilePath());
        warningSettingResDTO.setEnabled(warningSetting.getEnabled());
        warningSettingResDTO.setContent(warningSetting.getContent());
        return warningSettingResDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateWarningSetting(WarningSettingReqDTO warningSettingResDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        WarningSetting warningSetting = new WarningSetting();
        warningSetting.setId(warningSettingResDTO.getId());
        warningSetting.setUpdateTime(new Date());
        warningSetting.setFileName(warningSettingResDTO.getFileName());
        warningSetting.setFilePath(warningSettingResDTO.getFilePath());
        warningSetting.setSend(warningSettingResDTO.getSend());
        warningSetting.setPlay(warningSettingResDTO.getPlay());
        warningSetting.setGeneralLimit(warningSettingResDTO.getGeneralLimit());
        warningSetting.setSeverityLimit(warningSettingResDTO.getSeverityLimit());
        warningSetting.setWarningLimit(warningSettingResDTO.getWarningLimit());
        warningSettingMapper.updateSkipNull(warningSetting);
        WarningSetting byId = warningSettingMapper.findById(warningSetting.getId());
        redisDao.cacheWarningSetting(byId);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO changeWarningSettingStatus(Long id, Integer status)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        WarningSetting warningSetting = new WarningSetting();
        warningSetting.setId(id);
        warningSetting.setUpdateTime(new Date());
        warningSetting.setEnabled(status);
        warningSettingMapper.updateSkipNull(warningSetting);
        return responseDTO;
    }

    @Override
    public List<WarningSettingResDTO> getWarningSettingList()
    {
        List<WarningSetting> listAll = warningSettingMapper.getListAll();
        List<WarningSettingResDTO> dataList = new ArrayList<>();
        WarningSettingResDTO warningSettingResDTO;
        for(WarningSetting item : listAll)
        {
            warningSettingResDTO = new WarningSettingResDTO();
            warningSettingResDTO.setId(item.getId());
            warningSettingResDTO.setContent(item.getContent());
            warningSettingResDTO.setEnabled(item.getEnabled());
            warningSettingResDTO.setFileName(item.getFileName());
            warningSettingResDTO.setFilePath(item.getFilePath());
            warningSettingResDTO.setPlay(item.getPlay());
            warningSettingResDTO.setSend(item.getSend());
            warningSettingResDTO.setType(item.getType());
            warningSettingResDTO.setGeneralLimit(item.getGeneralLimit());
            warningSettingResDTO.setSeverityLimit(item.getSeverityLimit());
            warningSettingResDTO.setWarningLimit(item.getWarningLimit());
            dataList.add(warningSettingResDTO);
        }
        return dataList;
    }

    @Override
    public ListDTO<WarningManageResDTO> getWarningManageInfoListByType(Integer type, Long managerId, Date fromDate,
            Date toDate, Integer dispose, Page page)
    {
        ListDTO<WarningManageResDTO> listDTO = new ListDTO<>();
        List<WarningManageResDTO> list = new ArrayList<>();
        listDTO.setList(list);
        int total = warningManageMapper.countByWarningType(type, managerId, fromDate, toDate, dispose);
        listDTO.setTotal(total);
        long pages = page.getPages(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<WarningManageInfo> warningManageListByWarningType =
                    warningManageMapper.getWarningManageListByWarningType(type, managerId, fromDate, toDate, dispose);
            if(!CollectionUtils.isEmpty(warningManageListByWarningType))
            {
                WarningManageResDTO warningManageResDTOTemp;
                Set<Long> productIdsSet = new HashSet<>();
                Set<Long> clientIdsSet = new HashSet<>();
                Set<Long> managerIdsSet = new HashSet<>();
                for(WarningManageInfo item : warningManageListByWarningType)
                {
                    productIdsSet.add(item.getProductId());
                    clientIdsSet.add(item.getClientId());
                    managerIdsSet.add(item.getUserId());
                }
                List<Long> productIds = new ArrayList<>(productIdsSet);
                List<Long> clientIds = new ArrayList<>(clientIdsSet);
                List<Long> managerIds = new ArrayList<>(managerIdsSet);
                List<ClientInfoResDTO> clientList = null;
                List<ProductResDTO> productList = null;
                List<ManagerInfoResDTO> accountInfoList = null;
                if(!CollectionUtils.isEmpty(clientIds))
                {
                    clientList = clientRpcService.getClientListByIds(clientIds);
                }
                if(!CollectionUtils.isEmpty(productIds))
                {
                    productList = productRpcService.getProductList(productIds);
                }
                if(!CollectionUtils.isEmpty(managerIds))
                {
                    accountInfoList = managerRpcService.getAccountInfoList(managerIds);
                }
                Map<Long, ProductResDTO> productMap = new HashMap<>();
                Map<Long, ClientInfoResDTO> clientMap = new HashMap<>();
                Map<Long, ManagerInfoResDTO> userMap = new HashMap<>();
                if(!CollectionUtils.isEmpty(productList))
                {
                    for(ProductResDTO item : productList)
                    {
                        productMap.put(item.getId(), item);
                    }
                }
                if(!CollectionUtils.isEmpty(clientList))
                {
                    for(ClientInfoResDTO item : clientList)
                    {
                        clientMap.put(item.getClientId(), item);
                    }
                }
                if(!CollectionUtils.isEmpty(accountInfoList))
                {
                    for(ManagerInfoResDTO item : accountInfoList)
                    {
                        userMap.put(item.getManagerId(), item);
                    }
                }
                for(WarningManageInfo item : warningManageListByWarningType)
                {
                    warningManageResDTOTemp = new WarningManageResDTO();
                    warningManageResDTOTemp.setId(item.getId());
                    warningManageResDTOTemp.setWarningType(item.getWarningType());
                    warningManageResDTOTemp.setWarningAt(item.getCreateTime());
                    warningManageResDTOTemp.setThirdCode(item.getThirdCode());
                    warningManageResDTOTemp.setWarningCode(item.getWarningCode());
                    warningManageResDTOTemp.setDispose(item.getDispose());
                    warningManageResDTOTemp.setProductName(productMap.get(item.getProductId()) == null ? "" :
                            productMap.get(item.getProductId()).getName());
                    warningManageResDTOTemp.setCorpName(clientMap.get(item.getClientId()) == null ? "" :
                            clientMap.get(item.getClientId()).getCorpName());
                    warningManageResDTOTemp.setUserName(
                            userMap.get(item.getUserId()) == null ? "" : userMap.get(item.getUserId()).getName());
                    list.add(warningManageResDTOTemp);
                }
            }
        }
        return listDTO;
    }

    @Override
    public ListDTO<WarningManageDetailResDTO> getWarningDetailListByManageId(Long manageId, Page page)
    {
        ListDTO<WarningManageDetailResDTO> listDTO = new ListDTO<>();
        List<WarningManageDetailResDTO> dataList = new ArrayList<>();
        listDTO.setList(dataList);
        int total = warningManageDetailMapper.countByManageId(manageId);
        listDTO.setTotal(total);
        long pages = page.getPages(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<WarningManageDetail> list = warningManageDetailMapper.getListByManageId(manageId);
            WarningManageDetailResDTO warningManageDetailResDTO;
            Set<Long> productIdsSet = new HashSet<>();
            Set<Long> clientIdsSet = new HashSet<>();
            Set<Long> clientUserIdsSet = new HashSet<>();
            for(WarningManageDetail item : list)
            {
                productIdsSet.add(item.getProductId());
                clientIdsSet.add(item.getClientId());
                clientUserIdsSet.add(item.getClientUserId());
            }
            List<Long> productIds = new ArrayList<>(productIdsSet);
            List<Long> clientIds = new ArrayList<>(clientIdsSet);
            List<Long> clientUserIds = new ArrayList<>(clientUserIdsSet);
            List<ClientUserResDTO> clientUserList = clientRpcService.getClientUserListByIds(clientUserIds);
            List<ClientInfoResDTO> clientList = clientRpcService.getClientListByIds(clientIds);
            List<ProductResDTO> productList = productRpcService.getProductList(productIds);
            Map<Long, ProductResDTO> productMap = new HashMap<>();
            Map<Long, ClientInfoResDTO> clientMap = new HashMap<>();
            Map<Long, ClientUserResDTO> clientUserMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(productList))
            {
                for(ProductResDTO item : productList)
                {
                    productMap.put(item.getId(), item);
                }
            }
            if(!CollectionUtils.isEmpty(clientList))
            {
                for(ClientInfoResDTO item : clientList)
                {
                    clientMap.put(item.getClientId(), item);
                }
            }
            if(!CollectionUtils.isEmpty(clientUserList))
            {
                for(ClientUserResDTO item : clientUserList)
                {
                    clientUserMap.put(item.getId(), item);
                }
            }
            for(WarningManageDetail item : list)
            {
                warningManageDetailResDTO = new WarningManageDetailResDTO();
                dataList.add(warningManageDetailResDTO);
                warningManageDetailResDTO.setRequestIp(item.getRequestIp());
                warningManageDetailResDTO.setProductName(productMap.get(item.getProductId()) == null ? "" :
                        productMap.get(item.getProductId()).getName());
                warningManageDetailResDTO.setCorpName(clientMap.get(item.getClientId()) == null ? "" :
                        clientMap.get(item.getClientId()).getCorpName());
                warningManageDetailResDTO.setTime(item.getTime());
                warningManageDetailResDTO.setErrorType(item.getErrorType());
                warningManageDetailResDTO.setWarningAt(item.getTime());
                warningManageDetailResDTO.setUserName(clientUserMap.get(item.getClientUserId()) == null ? "" :
                        clientUserMap.get(item.getClientUserId()).getUsername());
            }
        }
        return listDTO;
    }

    @Override
    public WarningManageResDTO getWarningManageInfoById(Long id)
    {
        WarningManageResDTO warningManageResDTO = new WarningManageResDTO();
        WarningManageInfo warningManageInfo = warningManageMapper.getWarningManageById(id);
        if(warningManageInfo.getProductId() != null)
        {
            ProductResDTO productInfoData = productRpcService.getProductInfoData(warningManageInfo.getProductId());
            warningManageResDTO.setProductName(productInfoData != null ? productInfoData.getName() : "");
        }
        if(warningManageInfo.getClientId() != null)
        {
            ClientDetailResDTO clientInfo = clientRpcService.getClientInfo(warningManageInfo.getClientId());
            warningManageResDTO.setCorpName(clientInfo != null ? clientInfo.getCorpName() : "");
        }
        if(warningManageInfo.getUserId() != null)
        {
            UserInfoResDTO accountInfo = managerRpcService.getAccountInfo(warningManageInfo.getUserId());
            warningManageResDTO.setUserName(accountInfo != null ? accountInfo.getName() : "");
        }
        warningManageResDTO.setId(warningManageInfo.getId());
        warningManageResDTO.setWarningCode(warningManageInfo.getWarningCode());
        warningManageResDTO.setWarningAt(warningManageInfo.getCreateTime());
        warningManageResDTO.setWarningType(warningManageInfo.getWarningType());
        warningManageResDTO.setDispose(warningManageInfo.getDispose());
        warningManageResDTO.setRemark(warningManageInfo.getRemark());
        warningManageResDTO.setDisposeTime(warningManageInfo.getDisposeTime());
        return warningManageResDTO;
    }

    @Override
    @Transactional
    public ResponseDTO disposeWarningManage(WarningManageReqDTO warningManageReqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        WarningManage warningManage = new WarningManage();
        warningManage.setId(warningManageReqDTO.getId());
        warningManage.setDispose(warningManageReqDTO.getDispose());
        warningManage.setRemark(warningManageReqDTO.getRemark());
        warningManage.setUserId(warningManageReqDTO.getManagerId());
        warningManage.setDisposeTime(warningManageReqDTO.getDisposeTime());
        warningManageMapper.updateSkipNull(warningManage);
        return responseDTO;
    }

    @Override
    public List<WarningManageResDTO> getWarningDisposeManagerList()
    {
        List<WarningManageResDTO> list = new ArrayList<>();
        List<WarningManage> warningManageList = warningManageMapper.getWarningDisposeManagerList();
        Set<Long> managerIdsSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(warningManageList))
        {
            for(WarningManage item : warningManageList)
            {
                managerIdsSet.add(item.getUserId());
            }
            List<Long> managerIds = new ArrayList<>(managerIdsSet);
            List<ManagerInfoResDTO> userResDTOList = managerRpcService.getAccountInfoList(managerIds);
            WarningManageResDTO warningManageResDTO;
            for(ManagerInfoResDTO item : userResDTOList)
            {
                warningManageResDTO = new WarningManageResDTO();
                list.add(warningManageResDTO);
                warningManageResDTO.setUserId(item.getManagerId());
                warningManageResDTO.setUserName(item.getName());
            }
        }
        return list;
    }

    @Override
    public ListDTO<WarningOutResDTO> getWarningOutListByWarningType(Integer warningType, Page page)
    {
        ListDTO<WarningOutResDTO> listDTO = new ListDTO<>();
        List<WarningOutResDTO> dataList = new ArrayList<>();
        listDTO.setList(dataList);
        int total = warningOutMapper.countByWarningType(warningType);
        listDTO.setTotal(total);
        long pages = page.getPages(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);

            List<WarningOut> list = warningOutMapper.getListByWarningType(warningType);
            Set<Long> productIdsSet = new HashSet<>();
            Set<Long> clientIdsSet = new HashSet<>();
            for(WarningOut item : list)
            {
                productIdsSet.add(item.getProductId());
                clientIdsSet.add(item.getClientId());
            }
            List<Long> productIds = new ArrayList<>(productIdsSet);
            List<Long> clientIds = new ArrayList<>(clientIdsSet);
            List<ClientInfoResDTO> clientList = null;
            List<ProductResDTO> productList = null;
            if(!CollectionUtils.isEmpty(clientIds))
            {
                clientList = clientRpcService.getClientListByIds(clientIds);
            }
            if(!CollectionUtils.isEmpty(productIds))
            {
                productList = productRpcService.getProductList(productIds);
            }
            Map<Long, ProductResDTO> productMap = new HashMap<>();
            Map<Long, ClientInfoResDTO> clientMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(productList))
            {
                for(ProductResDTO item : productList)
                {
                    productMap.put(item.getId(), item);
                }
            }
            if(!CollectionUtils.isEmpty(clientList))
            {
                for(ClientInfoResDTO item : clientList)
                {
                    clientMap.put(item.getClientId(), item);
                }
            }
            WarningOutResDTO warningOutResDTO;
            for(WarningOut item : list)
            {
                warningOutResDTO = new WarningOutResDTO();
                dataList.add(warningOutResDTO);
                warningOutResDTO.setId(item.getId());
                warningOutResDTO.setWarningCode(item.getWarningCode());
                warningOutResDTO.setCount(item.getCount());
                warningOutResDTO.setLastTime(item.getLastTime());
                warningOutResDTO.setThirdCode(item.getThirdCode());
                warningOutResDTO.setLevel(item.getLevel());
                warningOutResDTO.setProductName(productMap.get(item.getProductId()) == null ? "" :
                        productMap.get(item.getProductId()).getName());
                warningOutResDTO.setCorpName(clientMap.get(item.getClientId()) == null ? "" :
                        clientMap.get(item.getClientId()).getCorpName());
            }
        }
        return listDTO;
    }

    @Override
    public ListDTO<WarningPacifyInfoResDTO> getWarningPacifyInfoList(List<Long> clientIds, Integer errorStatus,
            Integer dispose, Date fromDate, Date toDate, Page page)
    {
        ListDTO<WarningPacifyInfoResDTO> listDTO = new ListDTO<>();
        List<WarningPacifyInfoResDTO> list = new ArrayList<>();
        listDTO.setList(list);
        int total = warningPacifyInfoMapper.countBy(clientIds, errorStatus, dispose, fromDate, toDate);
        listDTO.setTotal(total);
        long pages = page.getPages(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<WarningPacifyInfo> pacifyInfoList = warningPacifyInfoMapper.getListBy(clientIds, errorStatus, dispose,
                    fromDate, toDate);
            Set<Long> clientIdsSet = new HashSet<>();
            Set<Long> pacifyIdsSet = new HashSet<>();
            Set<Long> productIdsSet = new HashSet<>();
            for(WarningPacifyInfo item : pacifyInfoList)
            {
                pacifyIdsSet.add(item.getPacifyId());
                clientIdsSet.add(item.getClientId());
            }
            List<ClientDetailResDTO> clientListByIds = clientRpcService.getClientDetailListByIds(
                    new ArrayList<>(clientIdsSet));
            Map<Long, ClientDetailResDTO> clientMap = new HashMap<>();
            for(ClientDetailResDTO item : clientListByIds)
            {
                clientMap.put(item.getClientId(), item);
            }
            List<WarningPacifyProduct> pacifyProductList = warningPacifyProductMapper.getListByPacifyIds(
                    new ArrayList<>(pacifyIdsSet));
            Map<Long, List<WarningPacifyProduct>> pacifyProductMap = new HashMap<>();
            List<WarningPacifyProduct> warningPacifyProductList;
            for(WarningPacifyProduct item : pacifyProductList)
            {
                warningPacifyProductList = pacifyProductMap.computeIfAbsent(item.getPacifyId(), k -> new ArrayList<>());
                warningPacifyProductList.add(item);
                productIdsSet.add(item.getProductId());
            }
            List<ProductResDTO> productList;
            if(!CollectionUtils.isEmpty(productIdsSet))
            {
                productList = productRpcService.getProductList(new ArrayList<>(productIdsSet));
            }
            else
            {
                productList = new ArrayList<>();
            }
            Map<Long, ProductResDTO> productMap = new HashMap<>();
            for(ProductResDTO item : productList)
            {
                productMap.put(item.getId(), item);
            }
            ClientDetailResDTO clientDetailResDTO;
            WarningPacifyInfoResDTO warningPacifyInfoResDTO;
            List<ClientContactResDTO> contacts;
            ProductResDTO productResDTO;
            for(WarningPacifyInfo item : pacifyInfoList)
            {
                clientDetailResDTO = clientMap.get(item.getClientId());
                warningPacifyProductList = pacifyProductMap.get(item.getPacifyId());
                warningPacifyInfoResDTO = new WarningPacifyInfoResDTO();
                list.add(warningPacifyInfoResDTO);
                warningPacifyInfoResDTO.setPacifyId(item.getPacifyId());
                warningPacifyInfoResDTO.setErrorStatus(item.getWarningStatus());
                warningPacifyInfoResDTO.setErrorTime(item.getWarningTime());
                warningPacifyInfoResDTO.setDispose(item.getDispose());
                if(clientDetailResDTO != null)
                {
                    warningPacifyInfoResDTO.setCorpName(clientDetailResDTO.getCorpName());
                    contacts = clientDetailResDTO.getContacts();
                    if(contacts != null && contacts.size() > 0)
                    {
                        warningPacifyInfoResDTO.setLinkName(contacts.get(0).getName());
                        warningPacifyInfoResDTO.setPhone(contacts.get(0).getPhone());
                        warningPacifyInfoResDTO.setContacts(contacts);
                    }
                }
                if(warningPacifyProductList != null && warningPacifyProductList.size() > 0)
                {
                    warningPacifyInfoResDTO.setProductCount(warningPacifyProductList.size());
                    productResDTO = productMap.get(warningPacifyProductList.get(0).getProductId());
                    warningPacifyInfoResDTO.setProductName(productResDTO == null ? "" : productResDTO.getName());
                }
                else
                {
                    warningPacifyInfoResDTO.setProductCount(0);
                }
            }
        }
        return listDTO;
    }

    @Override
    @Transactional
    public ResponseDTO disposeWarningPacify(WarningPacifyReqDTO warningPacifyReqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        WarningPacify warningPacify = new WarningPacify();
        warningPacify.setId(warningPacifyReqDTO.getId());
        warningPacify.setRemark(warningPacifyReqDTO.getRemark());
        warningPacify.setDispose(warningPacifyReqDTO.getDispose());
        warningPacify.setDisposeTime(warningPacifyReqDTO.getDisposeTime());
        warningPacify.setUserId(warningPacifyReqDTO.getManagerId());
        warningPacifyMapper.updateSkipNull(warningPacify);
        return responseDTO;
    }

    @Override
    public WarningPacifyInfoResDTO getWarningPacifyInfoById(Long id)
    {
        WarningPacifyInfoResDTO warningPacifyInfoResDTO = new WarningPacifyInfoResDTO();
        WarningPacifyInfo warningPacifyInfo = warningPacifyInfoMapper.findById(id);
        warningPacifyInfoResDTO.setPacifyId(warningPacifyInfo.getPacifyId());
        warningPacifyInfoResDTO.setWarningCode(warningPacifyInfo.getWarningCode());
        warningPacifyInfoResDTO.setWarningDisposeTime(warningPacifyInfo.getWarningDisposeTime());
        warningPacifyInfoResDTO.setPacifyDisposeTime(warningPacifyInfo.getPacifyDisposeTime());
        if(warningPacifyInfo.getWarningUserId() != null)
        {
            UserInfoResDTO accountInfo = managerRpcService.getAccountInfo(warningPacifyInfo.getWarningUserId());
            warningPacifyInfoResDTO.setWarningUserName(accountInfo != null ? accountInfo.getName() : "");
        }
        if(warningPacifyInfo.getPacifyUserId() != null)
        {
            UserInfoResDTO accountInfo = managerRpcService.getAccountInfo(warningPacifyInfo.getPacifyUserId());
            warningPacifyInfoResDTO.setPacifyUserName(accountInfo != null ? accountInfo.getName() : "");
        }
        ClientDetailResDTO clientInfo = clientRpcService.getClientInfo(warningPacifyInfo.getClientId());
        warningPacifyInfoResDTO.setCorpName(clientInfo != null ? clientInfo.getCorpName() : "");
        warningPacifyInfoResDTO.setErrorTime(warningPacifyInfo.getWarningTime());
        warningPacifyInfoResDTO.setWarningRemark(warningPacifyInfo.getWarningRemark());
        warningPacifyInfoResDTO.setPacifyRemark(warningPacifyInfo.getPacifyRemark());
        warningPacifyInfoResDTO.setClientId(warningPacifyInfo.getClientId());
        warningPacifyInfoResDTO.setErrorStatus(warningPacifyInfo.getWarningStatus());
        warningPacifyInfoResDTO.setDispose(warningPacifyInfo.getDispose());
        warningPacifyInfoResDTO.setContacts(clientInfo != null ? clientInfo.getContacts() : null);
        warningPacifyInfoResDTO.setWarningType(warningPacifyInfo.getWarningType());
        List<WarningPacifyProduct> warningPacifyProductList = warningPacifyProductMapper.getListByPacifyId(id);
        List<WarningPacifyProductResDTO> warningPacifyProductResDTOList = new ArrayList<>();
        warningPacifyInfoResDTO.setPacifyProducts(warningPacifyProductResDTOList);
        WarningPacifyProductResDTO warningPacifyProductResDTO;
        List<Long> productIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(warningPacifyProductList))
        {
            for(WarningPacifyProduct item : warningPacifyProductList)
            {
                productIds.add(item.getProductId());
            }
            List<ProductResDTO> productList = productRpcService.getProductList(productIds);
            Map<Long, ProductResDTO> productResDTOMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(productList))
            {
                for(ProductResDTO item : productList)
                {
                    productResDTOMap.put(item.getId(), item);
                }
            }
            for(WarningPacifyProduct item : warningPacifyProductList)
            {
                warningPacifyProductResDTO = new WarningPacifyProductResDTO();
                warningPacifyProductResDTOList.add(warningPacifyProductResDTO);
                warningPacifyProductResDTO.setPacifyId(item.getPacifyId());
                warningPacifyProductResDTO.setProductId(item.getProductId());
                warningPacifyProductResDTO.setProductName(productResDTOMap.get(item.getProductId()) != null ?
                        productResDTOMap.get(item.getProductId()).getName() : "");
            }
        }
        return warningPacifyInfoResDTO;
    }

    @Override
    public List<WarningSettingResDTO> getWarningSettingListByWarningType(Integer warningType)
    {
        List<WarningSetting> listAll = warningSettingMapper.getListByWarningType(warningType);
        List<WarningSettingResDTO> dataList = new ArrayList<>();
        WarningSettingResDTO warningSettingResDTO;
        for(WarningSetting item : listAll)
        {
            warningSettingResDTO = new WarningSettingResDTO();
            warningSettingResDTO.setId(item.getId());
            warningSettingResDTO.setContent(item.getContent());
            warningSettingResDTO.setEnabled(item.getEnabled());
            warningSettingResDTO.setFilePath(item.getFilePath());
            warningSettingResDTO.setFileName(item.getFileName());
            warningSettingResDTO.setPlay(item.getPlay());
            warningSettingResDTO.setSend(item.getSend());
            warningSettingResDTO.setType(item.getType());
            warningSettingResDTO.setGeneralLimit(item.getGeneralLimit());
            warningSettingResDTO.setSeverityLimit(item.getSeverityLimit());
            warningSettingResDTO.setWarningLimit(item.getWarningLimit());
            warningSettingResDTO.setWarningCode(item.getCode());
            dataList.add(warningSettingResDTO);
        }
        return dataList;
    }

}

package com.mingdong.bop.service.impl;

import com.mingdong.backend.service.BackendWarningService;
import com.mingdong.bop.component.FileUpload;
import com.mingdong.bop.component.Param;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ArticlesVO;
import com.mingdong.bop.model.FileResVO;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.model.SistemVO;
import com.mingdong.bop.model.WarningSettingVO;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.QueryStatus;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.constant.WarningCode;
import com.mingdong.core.constant.WarningType;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.SistemDTO;
import com.mingdong.core.model.dto.request.ArticlesReqDTO;
import com.mingdong.core.model.dto.request.IndustryReqDTO;
import com.mingdong.core.model.dto.request.PrivilegeReqDTO;
import com.mingdong.core.model.dto.request.RechargeTypeReqDTO;
import com.mingdong.core.model.dto.request.WarningManageReqDTO;
import com.mingdong.core.model.dto.request.WarningPacifyReqDTO;
import com.mingdong.core.model.dto.request.WarningSettingReqDTO;
import com.mingdong.core.model.dto.response.ArticlesDetailResDTO;
import com.mingdong.core.model.dto.response.ArticlesResDTO;
import com.mingdong.core.model.dto.response.ClientContactResDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.DictIndustryResDTO;
import com.mingdong.core.model.dto.response.PrivilegeResDTO;
import com.mingdong.core.model.dto.response.RechargeTypeResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.WarningManageDetailResDTO;
import com.mingdong.core.model.dto.response.WarningManageResDTO;
import com.mingdong.core.model.dto.response.WarningOutResDTO;
import com.mingdong.core.model.dto.response.WarningPacifyInfoResDTO;
import com.mingdong.core.model.dto.response.WarningPacifyProductResDTO;
import com.mingdong.core.model.dto.response.WarningSettingResDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.core.service.SystemRpcService;
import com.mingdong.core.util.DateCalculateUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemServiceImpl implements SystemService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private Param param;
    @Resource
    private CommonRpcService commonRpcService;
    @Resource
    private SystemRpcService systemRpcService;
    @Resource
    private ClientRpcService clientRpcService;
    @Resource
    private BackendWarningService backendWarningService;

    @Override
    public void checkIfIndustryExist(String code, RestResp resp)
    {
        resp.addData(Field.EXIST, commonRpcService.checkIfIndustryExist(code.toUpperCase()));
    }

    @Override
    public void addIndustryType(Long id, String code, String name, RestResp resp)
    {
        IndustryReqDTO reqDTO = new IndustryReqDTO();
        reqDTO.setCode(code.toUpperCase());
        reqDTO.setName(name);
        reqDTO.setParentId(id != null ? id : 0L);
        ResponseDTO respDTO = systemRpcService.addIndustryType(reqDTO);
        resp.setError(respDTO.getResult());
    }

    @Override
    public void editIndustryInfo(Long id, String code, String name, RestResp resp)
    {
        IndustryReqDTO reqDTO = new IndustryReqDTO();
        reqDTO.setId(id);
        reqDTO.setCode(code);
        reqDTO.setName(name);
        ResponseDTO respDTO = systemRpcService.editIndustryInfo(reqDTO);
        resp.setError(respDTO.getResult());
    }

    @Override
    public void editRechargeType(Integer rechargeTypeId, String name, String remark, RestResp resp)
    {
        RechargeTypeReqDTO rt = new RechargeTypeReqDTO();
        rt.setId(rechargeTypeId);
        rt.setName(name);
        rt.setRemark(remark);
        ResponseDTO res = systemRpcService.editRechargeType(rt);
        resp.setError(res.getResult());
    }

    @Override
    public void enableRechargeType(Integer rechargeTypeId, Integer enabled, RestResp resp)
    {
        RechargeTypeReqDTO rt = new RechargeTypeReqDTO();
        rt.setId(rechargeTypeId);
        rt.setEnabled(enabled);
        ResponseDTO res = systemRpcService.editRechargeType(rt);
        resp.setError(res.getResult());
    }

    @Override
    public List<Map<String, Object>> getHierarchyIndustry()
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ListDTO<DictIndustryResDTO> listDTO1 = systemRpcService.getIndustryList(0L, null);
        if(!CollectionUtils.isEmpty(listDTO1.getList()))
        {
            for(DictIndustryResDTO o : listDTO1.getList())
            {
                Map<String, Object> p = new HashMap<>();
                p.put(Field.ID, o.getId() + "");
                p.put(Field.CODE, o.getCode());
                p.put(Field.NAME, o.getName());
                p.put(Field.ENABLED, o.getEnabled());
                ListDTO<DictIndustryResDTO> listDTO2 = systemRpcService.getIndustryList(o.getId(), null);
                List<Map<String, Object>> subList = new ArrayList<>();
                if(!CollectionUtils.isEmpty(listDTO2.getList()))
                {
                    for(DictIndustryResDTO ind : listDTO2.getList())
                    {
                        Map<String, Object> c = new HashMap<>();
                        c.put(Field.ID, ind.getId() + "");
                        c.put(Field.CODE, ind.getCode());
                        c.put(Field.NAME, ind.getName());
                        c.put(Field.ENABLED, ind.getEnabled());
                        c.put(Field.PARENT_ID, ind.getParentId());
                        subList.add(c);
                    }
                }
                p.put(Field.SUB_LIST, subList);
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getHierarchyPrivilege()
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ListDTO<PrivilegeResDTO> listDTO1 = systemRpcService.getPrivilegeListByParent(0L);
        for(PrivilegeResDTO o1 : listDTO1.getList())
        {
            Map<String, Object> m1 = new HashMap<>();
            m1.put(Field.NAME, o1.getName());
            m1.put(Field.ID, o1.getPrivilegeId() + "");
            m1.put(Field.ENABLED, o1.getEnabled());
            List<Map<String, Object>> subList = new ArrayList<>();
            ListDTO<PrivilegeResDTO> listDTO2 = systemRpcService.getPrivilegeListByParent(o1.getPrivilegeId());
            for(PrivilegeResDTO o2 : listDTO2.getList())
            {
                Map<String, Object> m2 = new HashMap<>();
                m2.put(Field.NAME, o2.getName());
                m2.put(Field.ID, o2.getPrivilegeId() + "");
                m2.put(Field.ENABLED, o2.getEnabled());
                List<Map<String, Object>> thrList = new ArrayList<>();
                ListDTO<PrivilegeResDTO> listDTO3 = systemRpcService.getPrivilegeListByParent(o2.getPrivilegeId());
                for(PrivilegeResDTO o3 : listDTO3.getList())
                {
                    Map<String, Object> m3 = new HashMap<>();
                    m3.put(Field.NAME, o3.getName());
                    m3.put(Field.ID, o3.getPrivilegeId() + "");
                    m3.put(Field.ENABLED, o3.getEnabled());
                    thrList.add(m3);
                }
                m2.put(Field.THR_LIST, thrList);
                subList.add(m2);

            }
            m1.put(Field.SUB_LIST, subList);
            list.add(m1);
        }
        return list;
    }

    @Override
    public void editPrivilegeInfo(Long privilegeId, String name, RestResp resp)
    {
        PrivilegeReqDTO privilegeResDTO = new PrivilegeReqDTO();
        privilegeResDTO.setPrivilegeId(privilegeId);
        privilegeResDTO.setName(name);
        ResponseDTO responseDTO = systemRpcService.editPrivilegeInfo(privilegeResDTO);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public List<Dict> getRechargeDict()
    {
        ListDTO<Dict> listDTO = commonRpcService.getRechargeTypeDict();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            return listDTO.getList();
        }
        return new ArrayList<>();
    }

    @Override
    public void addRechargeType(String name, String remark, RestResp resp)
    {
        ResponseDTO res = systemRpcService.addRechargeType(name, remark);
        resp.setError(res.getResult());
    }

    @Override
    public List<Map<String, Object>> getIndustryList(Long parentId, Integer enabled)
    {
        ListDTO<DictIndustryResDTO> listDTO = systemRpcService.getIndustryList(parentId, enabled);
        List<DictIndustryResDTO> dataList = listDTO.getList();
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(dataList))
        {
            for(DictIndustryResDTO parent : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, parent.getId() + "");
                map.put(Field.NAME, parent.getName());
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> getInitIndustryMap()
    {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> parentIndustryList = new ArrayList<>();
        List<Map<String, Object>> industryList = new ArrayList<>();
        ListDTO<DictIndustryResDTO> listDTO = systemRpcService.getIndustryList(0L, TrueOrFalse.TRUE);
        List<DictIndustryResDTO> parentList = listDTO.getList();
        if(!CollectionUtils.isEmpty(parentList))
        {
            for(DictIndustryResDTO industry : parentList)
            {
                Map<String, Object> pm = new HashMap<>();
                parentIndustryList.add(pm);
                pm.put(Field.ID, industry.getId() + "");
                pm.put(Field.NAME, industry.getName());
            }
            ListDTO<DictIndustryResDTO> listDTO2 = systemRpcService.getIndustryList(parentList.get(0).getId(),
                    TrueOrFalse.TRUE);
            List<DictIndustryResDTO> childList = listDTO2.getList();
            if(!CollectionUtils.isEmpty(childList))
            {
                for(DictIndustryResDTO industry : childList)
                {
                    Map<String, Object> pm = new HashMap<>();
                    pm.put(Field.NAME, industry.getName());
                    industryList.add(pm);
                    pm.put(Field.ID, industry.getId() + "");
                }
            }
        }
        map.put(Field.PARENT_INDUSTRY, parentIndustryList);
        map.put(Field.INDUSTRY, industryList);
        return map;
    }

    @Override
    public String getIndustryName(Long industryId)
    {
        String industryName = redisDao.getIndustryInfo(industryId);
        if(StringUtils.isNullBlank(industryName))
        {
            cacheAllIndustryData();
            industryName = redisDao.getIndustryInfo(industryId);
        }
        return industryName == null ? "" : industryName;
    }

    @Override
    public Map<String, String> cacheSystemModule()
    {
        Map<String, String> map = new HashMap<>();
        ListDTO<PrivilegeResDTO> listDTO = systemRpcService.getPrivilegeByLevel(3);
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(PrivilegeResDTO p : listDTO.getList())
            {
                map.put("m" + p.getPrivilegeId(), p.getName());
            }
        }
        redisDao.setSystemModule(map);
        return map;
    }

    @Override
    public Map<String, Object> getGlobalSetting()
    {
        Map<String, Object> map = new HashMap<>();
        SistemDTO sistemDTO = systemRpcService.getSystemSetting();
        map.put(Field.CLIENT_USER_MAX, sistemDTO.getClientUserMax());
        map.put(Field.SERVICE_QQ, sistemDTO.getServiceQQ());
        return map;
    }

    @Override
    public void setGlobalSetting(SistemVO sistemVO)
    {
        SistemDTO sistemDTO = new SistemDTO();
        sistemDTO.setClientUserMax(sistemVO.getClientUserMax());
        sistemDTO.setServiceQQ(sistemVO.getServiceQQ());
        systemRpcService.editSystemSetting(sistemDTO);
    }

    @Override
    public void changeIndustryStatus(Long industryTypeId, Integer enabled, RestResp resp)
    {
        ResponseDTO responseDTO = systemRpcService.changeIndustryStatus(industryTypeId, enabled);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public void setModuleStatus(List<Long> moduleIdList, Integer status)
    {
        systemRpcService.setModuleStatus(status, moduleIdList);
    }

    @Override
    public void getRechargeTypeList(RestListResp res)
    {
        ListDTO<RechargeTypeResDTO> dto = systemRpcService.getRechargeList();
        res.setTotal(dto.getTotal());
        List<Map<String, Object>> list = new ArrayList<>();
        for(RechargeTypeResDTO o : dto.getList())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, o.getId() + "");
            m.put(Field.NAME, o.getName());
            m.put(Field.REMARK, o.getRemark());
            m.put(Field.STATUS, o.getEnabled());
            m.put(Field.ADD_AT, DateUtils.format(o.getAddAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            list.add(m);
        }
        res.setList(list);
    }

    @Override
    public List<Dict> getAccountRoleDict()
    {
        ListDTO<Dict> listDTO = commonRpcService.getAdminGroupDict();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            return listDTO.getList();
        }
        return new ArrayList<>();
    }

    @Override
    public void getArticlesList(Page page, RestListResp res)
    {
        ListDTO<ArticlesResDTO> listDTO = systemRpcService.getArticlesList(page);
        res.setTotal(listDTO.getTotal());
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(ArticlesResDTO o : listDTO.getList())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.ID, o.getId() + "");
                m.put(Field.PUBLISH_TIME, DateUtils.format(o.getPublishTime(), DateFormat.YYYY_MM_DD));
                m.put(Field.TYPE, o.getType());
                m.put(Field.STATUS, o.getPublished());
                m.put(Field.TITLE, o.getTitle());
                m.put(Field.ORDER_ID, o.getOrderId());
                list.add(m);
            }
        }
        res.setList(list);
    }

    @Override
    public void addArticles(MultipartFile upfile, ArticlesVO articlesVO, RestResp resp)
    {
        FileResVO fileResVO = null;
        try
        {
            fileResVO = FileUpload.fileUploadOne(upfile, param.getSaveFilePath(), null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        ArticlesReqDTO articlesReqDTO = new ArticlesReqDTO();
        articlesReqDTO.setId(articlesVO.getId());
        articlesReqDTO.setAuthor(articlesVO.getAuthor());
        articlesReqDTO.setContent(articlesVO.getContent());
        articlesReqDTO.setDeleted(TrueOrFalse.FALSE);
        articlesReqDTO.setPublished(articlesVO.getPublished());
        articlesReqDTO.setImagePath(
                fileResVO == null ? null : (param.getFileNginxUrl() + fileResVO.getFileOtherPath()));
        articlesReqDTO.setOrderId(articlesVO.getOrderId());
        articlesReqDTO.setSynopsis(articlesVO.getSynopsis());
        articlesReqDTO.setTitle(articlesVO.getTitle());
        articlesReqDTO.setType(articlesVO.getType());
        articlesReqDTO.setPublishTime(articlesVO.getPublishTime());
        articlesReqDTO.setUserId(RequestThread.getOperatorId());
        try
        {
            ResponseDTO responseDTO = systemRpcService.addArticles(articlesReqDTO);
            if(responseDTO.getResult() != RestResult.SUCCESS)
            {
                resp.setError(responseDTO.getResult());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            resp.setError(RestResult.SYSTEM_ERROR);
        }
    }

    @Override
    public void updateArticles(MultipartFile upfile, ArticlesVO articlesVO, RestResp resp)
    {
        FileResVO fileResVO = null;
        if(upfile != null)
        {
            try
            {
                fileResVO = FileUpload.fileUploadOne(upfile, param.getSaveFilePath(), null);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        ArticlesReqDTO articlesReqDTO = new ArticlesReqDTO();
        articlesReqDTO.setId(articlesVO.getId());
        articlesReqDTO.setAuthor(articlesVO.getAuthor());
        articlesReqDTO.setContent(articlesVO.getContent());
        articlesReqDTO.setPublished(articlesVO.getPublished());
        articlesReqDTO.setImagePath(
                fileResVO == null ? null : (param.getFileNginxUrl() + fileResVO.getFileOtherPath()));
        articlesReqDTO.setOrderId(articlesVO.getOrderId());
        articlesReqDTO.setSynopsis(articlesVO.getSynopsis());
        articlesReqDTO.setTitle(articlesVO.getTitle());
        articlesReqDTO.setType(articlesVO.getType());
        articlesReqDTO.setPublishTime(articlesVO.getPublishTime());
        try
        {
            ResponseDTO responseDTO = systemRpcService.updateArticles(articlesReqDTO);
            if(responseDTO.getResult() != RestResult.SUCCESS)
            {
                resp.setError(responseDTO.getResult());
                return;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            resp.setError(RestResult.SYSTEM_ERROR);
        }
    }

    @Override
    public Map<String, Object> getArticlesInfo(Long id)
    {
        Map<String, Object> map = new HashMap<>();
        ArticlesDetailResDTO articlesDetailResDTO = systemRpcService.getArticlesInfo(id);
        map.put(Field.ID, articlesDetailResDTO.getId() + "");
        map.put(Field.TITLE, articlesDetailResDTO.getTitle());
        map.put(Field.TYPE, articlesDetailResDTO.getType());
        map.put(Field.AUTHOR, articlesDetailResDTO.getAuthor());
        map.put(Field.CONTENT, articlesDetailResDTO.getContent());
        map.put(Field.IMAGE_PATH, articlesDetailResDTO.getImagePath());
        map.put(Field.PUBLISHED, articlesDetailResDTO.getPublished());
        map.put(Field.PUBLISH_TIME, DateUtils.format(articlesDetailResDTO.getPublishTime(), DateFormat.YYYY_MM_DD));
        map.put(Field.ORDER_ID, articlesDetailResDTO.getOrderId());
        map.put(Field.SYNOPSIS, articlesDetailResDTO.getSynopsis());
        return map;
    }

    @Override
    public void deleteArticlesById(Long id, RestResp resp)
    {
        ResponseDTO responseDTO = systemRpcService.deleteArticlesById(id);
        if(responseDTO.getResult() != RestResult.SUCCESS)
        {
            resp.setError(responseDTO.getResult());
            return;
        }
    }

    @Override
    public Map<String, Object> getHomeData()
    {
        Map<String, Object> map = new HashMap<>();
        Date currentDay = new Date();
        Date monthFirst = DateCalculateUtils.getCurrentMonthFirst(currentDay, true);
        BigDecimal amount = clientRpcService.getClientRechargeStatsByDate(monthFirst, currentDay,
                RequestThread.isManager() ? null : RequestThread.getOperatorId());
        Integer clientCountByDate = clientRpcService.getClientCountByDate(monthFirst, currentDay,
                RequestThread.isManager() ? null : RequestThread.getOperatorId());
        Integer allClientCount = clientRpcService.getAllClientCount(
                RequestThread.isManager() ? null : RequestThread.getOperatorId());
        map.put(Field.AMOUNT, NumberUtils.formatAmount(amount));
        map.put(Field.CLIENT_COUNT_BY_DATE, clientCountByDate);
        map.put(Field.CLIENT_COUNT_ALL, allClientCount);
        ListDTO<ClientInfoResDTO> clientInfoListBy = clientRpcService.getClientInfoListBy(null, null, null,
                RequestThread.isManager() ? null : RequestThread.getOperatorId(), null);
        List<Long> clientIds = new ArrayList<>();
        List<ClientInfoResDTO> list = clientInfoListBy.getList();
        if(!CollectionUtils.isEmpty(list))
        {
            for(ClientInfoResDTO item : list)
            {
                clientIds.add(item.getClientId());
            }
        }
        if(clientIds.size() == 0)
        {
            map.put(Field.PACIFY_COUNT, 0);
            return map;
        }
        ListDTO<WarningPacifyInfoResDTO> warningPacifyInfoList = backendWarningService.getWarningPacifyInfoList(
                clientIds, null, TrueOrFalse.FALSE, null, null, new Page(1, 1));
        map.put(Field.PACIFY_COUNT, warningPacifyInfoList.getTotal());
        return map;
    }

    @Override
    public void getWarningSettingList(RestResp res)
    {
        List<WarningSettingResDTO> dataList = backendWarningService.getWarningSettingList();
        List<Map<String, Object>> product = new ArrayList<>();
        List<Map<String, Object>> client = new ArrayList<>();
        List<Map<String, Object>> other = new ArrayList<>();
        Map<String, Object> mapTemp;
        for(WarningSettingResDTO item : dataList)
        {
            mapTemp = new HashMap<>();
            mapTemp.put(Field.ID, item.getId() + "");
            mapTemp.put(Field.CONTENT, item.getContent());
            mapTemp.put(Field.SEND, item.getSend());
            mapTemp.put(Field.PLAY, item.getPlay());
            mapTemp.put(Field.FILE_NAME, item.getFileName());
            mapTemp.put(Field.FILE_PATH, item.getFilePath());
            mapTemp.put(Field.GENERAL_LIMIT, item.getGeneralLimit());
            mapTemp.put(Field.SEVERITY_LIMIT, item.getSeverityLimit());
            mapTemp.put(Field.WARNING_LIMIT, item.getWarningLimit());
            mapTemp.put(Field.ENABLED, item.getEnabled());
            if(WarningType.PRODUCT.equals(item.getType()))
            {
                product.add(mapTemp);
            }
            else if(WarningType.CLIENT.equals(item.getType()))
            {
                client.add(mapTemp);
            }
            else if(WarningType.THIRD.equals(item.getType()))
            {
                other.add(mapTemp);
            }
        }
        res.addData(Field.WARNING_PRODUCT, product);
        res.addData(Field.WARNING_CLIENT, client);
        res.addData(Field.WARNING_OTHER, other);
    }

    @Override
    public Map<String, Object> getWarningSetting(Long id)
    {
        Map<String, Object> map = new HashMap<>();
        WarningSettingResDTO warningSettingResDTO = backendWarningService.getWarningSetting(id);
        if(warningSettingResDTO != null)
        {
            map.put(Field.ID, warningSettingResDTO.getId());
            map.put(Field.SEND, warningSettingResDTO.getSend());
            map.put(Field.PLAY, warningSettingResDTO.getPlay());
            map.put(Field.CONTENT, warningSettingResDTO.getContent());
            map.put(Field.FILE_NAME, warningSettingResDTO.getFileName());
            map.put(Field.FILE_PATH, warningSettingResDTO.getFilePath());
            map.put(Field.GENERAL_LIMIT, warningSettingResDTO.getGeneralLimit());
            map.put(Field.SEVERITY_LIMIT, warningSettingResDTO.getSeverityLimit());
            map.put(Field.WARNING_LIMIT, warningSettingResDTO.getWarningLimit());
        }
        return map;
    }

    @Override
    public void updateWarningSetting(MultipartFile upfile, WarningSettingVO warningSettingVO, RestResp resp)
    {
        WarningSettingResDTO warningSetting = backendWarningService.getWarningSetting(warningSettingVO.getId());
        if(StringUtils.isNullBlank(warningSetting.getFileName()) && upfile == null)
        {
            resp.setError(RestResult.FILE_NOT_NULL);
            return;
        }
        FileResVO fileResVO = null;
        if(upfile != null)
        {
            try
            {
                fileResVO = FileUpload.fileUploadOne(upfile, param.getSaveFilePath(), new String[]{"mp3"});
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        WarningSettingReqDTO warningSettingReqDTO = new WarningSettingReqDTO();
        warningSettingReqDTO.setId(warningSettingVO.getId());
        warningSettingReqDTO.setFileName(fileResVO == null ? null : fileResVO.getFileName());
        warningSettingReqDTO.setFilePath(
                fileResVO == null ? null : (param.getFileNginxUrl() + fileResVO.getFileOtherPath()));
        warningSettingReqDTO.setSend(warningSettingVO.getSend());
        warningSettingReqDTO.setPlay(warningSettingVO.getPlay());
        warningSettingReqDTO.setGeneralLimit(warningSettingVO.getGeneralLimit());
        warningSettingReqDTO.setSeverityLimit(warningSettingVO.getSeverityLimit());
        warningSettingReqDTO.setWarningLimit(warningSettingVO.getWarningLimit());
        try
        {
            ResponseDTO responseDTO = backendWarningService.updateWarningSetting(warningSettingReqDTO);
            if(responseDTO.getResult() != RestResult.SUCCESS)
            {
                resp.setError(responseDTO.getResult());
                return;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            resp.setError(RestResult.SYSTEM_ERROR);
        }
    }

    @Override
    public void changeWarningSettingStatus(Long id, Integer status, RestResp resp)
    {
        ResponseDTO responseDTO = backendWarningService.changeWarningSettingStatus(id, status);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public Integer getDefaultSubUserQty()
    {
        SistemDTO sistemDTO = systemRpcService.getSystemSetting();
        return sistemDTO.getClientUserMax();
    }

    @Override
    public void getWarningManageList(RestResp res)
    {
        Page page = new Page(1, 6);
        ListDTO<WarningManageResDTO> productListDTO = backendWarningService.getWarningManageInfoListByType(
                WarningType.PRODUCT.getId(), null, null, null, TrueOrFalse.FALSE, page);
        List<WarningManageResDTO> productList = productListDTO.getList();
        ListDTO<WarningManageResDTO> clientListDTO = backendWarningService.getWarningManageInfoListByType(
                WarningType.CLIENT.getId(), null, null, null, TrueOrFalse.FALSE, page);
        List<WarningManageResDTO> clientList = clientListDTO.getList();
        ListDTO<WarningManageResDTO> thirdListDTO = new ListDTO<>();//TODO 第三方
        List<WarningManageResDTO> thirdList = thirdListDTO.getList();
        Map<String, Object> mapTemp;
        List<Map<String, Object>> productMapList = new ArrayList<>();
        List<Map<String, Object>> clientMapList = new ArrayList<>();
        List<Map<String, Object>> thirdMapList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(productList))
        {
            for(WarningManageResDTO item : productList)
            {
                mapTemp = new HashMap<>();
                productMapList.add(mapTemp);
                mapTemp.put(Field.WARNING_NAME, WarningCode.findByCode(item.getWarningCode()).getName());
                mapTemp.put(Field.PRODUCT_NAME, item.getProductName());
                mapTemp.put(Field.WARNING_AT, DateUtils.format(item.getWarningAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            }
        }
        if(!CollectionUtils.isEmpty(clientList))
        {
            for(WarningManageResDTO item : clientList)
            {
                mapTemp = new HashMap<>();
                clientMapList.add(mapTemp);
                mapTemp.put(Field.WARNING_NAME, WarningCode.findByCode(item.getWarningCode()).getName());
                mapTemp.put(Field.CORP_NAME, item.getCorpName());
                mapTemp.put(Field.WARNING_AT, DateUtils.format(item.getWarningAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            }
        }
        if(!CollectionUtils.isEmpty(thirdList))
        {
            for(WarningManageResDTO item : thirdList)
            {
                mapTemp = new HashMap<>();
                thirdMapList.add(mapTemp);
                mapTemp.put(Field.WARNING_NAME, WarningCode.findByCode(item.getWarningCode()).getName());
                mapTemp.put(Field.NAME, "");
                mapTemp.put(Field.WARNING_AT, DateUtils.format(item.getWarningAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            }
        }
        res.addData(Field.WARNING_PRODUCT_LIST, productMapList);
        res.addData(Field.WARNING_CLIENT_LIST, clientMapList);
        res.addData(Field.WARNING_THIRD_LIST, thirdMapList);
    }

    @Override
    public void getWarningManageListByWarningType(Integer type, Long managerId, Date fromDate, Date toDate,
            Integer dispose, RestListResp res, Page page)
    {
        ListDTO<WarningManageResDTO> productListDTO = backendWarningService.getWarningManageInfoListByType(type,
                managerId, fromDate, toDate, dispose, page);
        List<WarningManageResDTO> list = productListDTO.getList();
        List<Map<String, Object>> dataList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list))
        {
            Map<String, Object> mapTemp;
            WarningType warningTypeTemp;
            for(WarningManageResDTO item : list)
            {
                mapTemp = new HashMap<>();
                dataList.add(mapTemp);
                mapTemp.put(Field.ID, item.getId() + "");
                mapTemp.put(Field.WARNING_NAME, WarningType.findById(item.getWarningType()).getName() +
                        WarningCode.findByCode(item.getWarningCode()).getName());
                mapTemp.put(Field.WARNING_AT, DateUtils.format(item.getWarningAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                mapTemp.put(Field.MANAGER_NAME, item.getUserName());
                mapTemp.put(Field.WARNING_CODE, item.getWarningCode());
                warningTypeTemp = WarningType.findById(item.getWarningType());
                String name = "";
                if(warningTypeTemp == WarningType.PRODUCT)
                {
                    name = item.getProductName();
                }
                else if(warningTypeTemp == WarningType.CLIENT)
                {
                    name = item.getCorpName();
                }
                else if(warningTypeTemp == WarningType.THIRD)
                {
                    //TODO 第三方
                }
                mapTemp.put(Field.NAME, name);
            }
        }
        res.setTotal(productListDTO.getTotal());
        res.setList(dataList);
    }

    @Override
    public Map<String, Object> getWarningManageById(Long id)
    {
        Map<String, Object> map = new HashMap<>();
        WarningManageResDTO warningManageResDTO = backendWarningService.getWarningManageInfoById(id);
        map.put(Field.ID, warningManageResDTO.getId() + "");
        map.put(Field.TYPE, warningManageResDTO.getWarningType());
        map.put(Field.PRODUCT_NAME, warningManageResDTO.getProductName());
        map.put(Field.CORP_NAME, warningManageResDTO.getCorpName());
        map.put(Field.WARNING_NAME, WarningCode.findByCode(warningManageResDTO.getWarningCode()).getName());
        map.put(Field.WARNING_AT, DateUtils.format(warningManageResDTO.getWarningAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
        map.put(Field.DISPOSE, warningManageResDTO.getDispose());
        map.put(Field.REMARK, warningManageResDTO.getRemark());
        map.put(Field.MANAGER_NAME, warningManageResDTO.getUserName());
        map.put(Field.WARNING_DISPOSE_TIME,
                DateUtils.format(warningManageResDTO.getDisposeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
        return map;
    }

    @Override
    public void getWarningDetailListByManageId(Long manageId, Page page, RestListResp res)
    {
        ListDTO<WarningManageDetailResDTO> listDTO = backendWarningService.getWarningDetailListByManageId(manageId,
                page);
        List<WarningManageDetailResDTO> list = listDTO.getList();
        res.setTotal(listDTO.getTotal());
        List<Map<String, Object>> dataList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list))
        {
            Map<String, Object> mapTemp;
            for(WarningManageDetailResDTO item : list)
            {
                mapTemp = new HashMap<>();
                dataList.add(mapTemp);
                mapTemp.put(Field.WARNING_AT, DateUtils.format(item.getWarningAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                mapTemp.put(Field.HOST, item.getRequestIp());
                mapTemp.put(Field.USERNAME, item.getUserName());
                mapTemp.put(Field.PRODUCT_NAME, item.getProductId());
                mapTemp.put(Field.CORP_NAME, item.getCorpName());
                mapTemp.put(Field.ERROR_NAME, QueryStatus.getByCode(item.getErrorType()).getName());
            }
        }
        res.setList(dataList);
    }

    @Override
    public void updateWarningManageDispose(Long id, String remark, RestResp res)
    {
        WarningManageReqDTO warningManageReqDTO = new WarningManageReqDTO();
        warningManageReqDTO.setId(id);
        warningManageReqDTO.setDispose(TrueOrFalse.TRUE);
        warningManageReqDTO.setRemark(remark);
        warningManageReqDTO.setManagerId(RequestThread.getOperatorId());
        warningManageReqDTO.setDisposeTime(new Date());
        ResponseDTO responseDTO = backendWarningService.disposeWarningManage(warningManageReqDTO);
        if(responseDTO.getResult() != RestResult.SUCCESS)
        {
            res.setError(responseDTO.getResult());
            return;
        }
    }

    @Override
    public List<Map<String, Object>> getWarningDisposeManagerList()
    {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<WarningManageResDTO> list = backendWarningService.getWarningDisposeManagerList();
        if(!CollectionUtils.isEmpty(list))
        {
            Map<String, Object> mapTemp;
            for(WarningManageResDTO item : list)
            {
                mapTemp = new HashMap<>();
                dataList.add(mapTemp);
                mapTemp.put(Field.ID, item.getUserId());
                mapTemp.put(Field.NAME, item.getUserName());
            }
        }
        return dataList;
    }

    @Override
    public void getWarningOutListByWarningType(Integer warningType, RestListResp res, Page page)
    {
        ListDTO<WarningOutResDTO> warningOutResDTOS = backendWarningService.getWarningOutListByWarningType(warningType,
                page);
        res.setTotal(warningOutResDTOS.getTotal());
        List<WarningOutResDTO> list = warningOutResDTOS.getList();
        List<Map<String, Object>> dataList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list))
        {
            Map<String, Object> mapTemp;
            for(WarningOutResDTO item : list)
            {
                mapTemp = new HashMap<>();
                dataList.add(mapTemp);
                mapTemp.put(Field.ID, item.getId() + "");
                mapTemp.put(Field.WARNING_NAME, WarningCode.findByCode(item.getWarningCode()).getName());
                mapTemp.put(Field.PRODUCT_NAME, item.getProductName());
                mapTemp.put(Field.LEVEL, item.getLevel());
                mapTemp.put(Field.COUNT, item.getCount());
                mapTemp.put(Field.LAST_TIME, DateUtils.format(item.getLastTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            }
        }
        res.setList(dataList);
    }

    @Override
    public void getWarningPacifyInfoList(String keyword, Integer errorStatus, Integer dispose, Date fromDate,
            Date toDate, RestListResp res, Page page)
    {
        List<Long> clientIds = null;
        if(!StringUtils.isNullBlank(keyword) || !RequestThread.isManager())
        {
            clientIds = new ArrayList<>();
            ListDTO<ClientInfoResDTO> clientInfoListBy = clientRpcService.getClientInfoListBy(keyword, null, null,
                    RequestThread.isManager() ? null : RequestThread.getOperatorId(), null);
            List<ClientInfoResDTO> list = clientInfoListBy.getList();
            if(!CollectionUtils.isEmpty(list))
            {
                for(ClientInfoResDTO item : list)
                {
                    clientIds.add(item.getClientId());
                }
            }
        }
        List<Map<String, Object>> list = new ArrayList<>();
        res.setList(list);
        if(clientIds != null && clientIds.size() == 0)
        {
            return;
        }
        ListDTO<WarningPacifyInfoResDTO> listDTO = backendWarningService.getWarningPacifyInfoList(clientIds,
                errorStatus, dispose, fromDate, toDate, page);
        res.setTotal(listDTO.getTotal());
        List<WarningPacifyInfoResDTO> dataList = listDTO.getList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            Map<String, Object> mapTemp;
            for(WarningPacifyInfoResDTO item : dataList)
            {
                mapTemp = new HashMap<>();
                list.add(mapTemp);
                mapTemp.put(Field.ID, item.getPacifyId() + "");
                mapTemp.put(Field.CORP_NAME, item.getCorpName());
                mapTemp.put(Field.LINK_NAME, item.getLinkName());
                mapTemp.put(Field.PHONE, item.getPhone());
                mapTemp.put(Field.PRODUCT_NAME, item.getProductName() +
                        (item.getProductCount() > 1 ? ("（" + item.getProductCount() + "）") : ""));
                mapTemp.put(Field.STATUS, item.getErrorStatus());
                mapTemp.put(Field.ERROR_TIME, DateUtils.format(item.getErrorTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                mapTemp.put(Field.DISPOSE, item.getDispose());
            }
        }
    }

    @Override
    public void updatePacifyDispose(Long pacifyId, String remark, RestResp resp)
    {
        WarningPacifyReqDTO warningPacifyReqDTO = new WarningPacifyReqDTO();
        warningPacifyReqDTO.setId(pacifyId);
        warningPacifyReqDTO.setRemark(remark);
        warningPacifyReqDTO.setDispose(TrueOrFalse.TRUE);
        warningPacifyReqDTO.setDisposeTime(new Date());
        warningPacifyReqDTO.setManagerId(RequestThread.getOperatorId());
        ResponseDTO responseDTO = backendWarningService.disposeWarningPacify(warningPacifyReqDTO);
        if(responseDTO.getResult() != RestResult.SUCCESS)
        {
            resp.setError(responseDTO.getResult());
        }
    }

    @Override
    public Map<String, Object> getWarningPacifyDetailById(Long id)
    {
        Map<String, Object> dataMap = new HashMap<>();
        WarningPacifyInfoResDTO warningPacifyInfoResDTO = backendWarningService.getWarningPacifyInfoById(id);
        dataMap.put(Field.ID, warningPacifyInfoResDTO.getPacifyId() + "");
        dataMap.put(Field.CORP_NAME, warningPacifyInfoResDTO.getCorpName());
        dataMap.put(Field.WARNING_NAME, WarningType.findById(warningPacifyInfoResDTO.getWarningType()).getName() +
                WarningCode.findByCode(warningPacifyInfoResDTO.getWarningCode()).getName());
        dataMap.put(Field.ERROR_TIME,
                DateUtils.format(warningPacifyInfoResDTO.getErrorTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
        dataMap.put(Field.STATUS, warningPacifyInfoResDTO.getErrorStatus());
        dataMap.put(Field.DISPOSE, warningPacifyInfoResDTO.getDispose());
        dataMap.put(Field.WARNING_USER_NAME, warningPacifyInfoResDTO.getWarningUserName());
        dataMap.put(Field.PACIFY_USER_NAME, warningPacifyInfoResDTO.getPacifyUserName());
        dataMap.put(Field.WARNING_DISPOSE_TIME,
                DateUtils.format(warningPacifyInfoResDTO.getWarningDisposeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
        dataMap.put(Field.PACIFY_DISPOSE_TIME,
                DateUtils.format(warningPacifyInfoResDTO.getPacifyDisposeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
        dataMap.put(Field.WARNING_REMARK, warningPacifyInfoResDTO.getWarningRemark());
        dataMap.put(Field.PACIFY_REMARK, warningPacifyInfoResDTO.getPacifyRemark());
        List<Map<String, Object>> contactList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(warningPacifyInfoResDTO.getContacts()))
        {
            for(ClientContactResDTO o : warningPacifyInfoResDTO.getContacts())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.NAME, o.getName());
                m.put(Field.POSITION, o.getPosition());
                m.put(Field.EMAIL, o.getEmail());
                m.put(Field.PHONE, o.getPhone());
                m.put(Field.IS_GENERAL, o.getGeneral());
                contactList.add(m);
            }
        }
        dataMap.put(Field.CONTACT_LIST, contactList);
        List<Map<String, Object>> productList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(warningPacifyInfoResDTO.getPacifyProducts()))
        {
            for(WarningPacifyProductResDTO o : warningPacifyInfoResDTO.getPacifyProducts())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.NAME, o.getProductName());
                productList.add(m);
            }
        }
        dataMap.put(Field.PRODUCT_LIST, productList);
        return dataMap;
    }

    @Override
    public Map<String, Object> getWarningSettingListByWarningType(Integer warningType)
    {
        Map<String, Object> dataMap = new HashMap<>();
        List<WarningSettingResDTO> settingResDTOList = backendWarningService.getWarningSettingListByWarningType(
                warningType);
        List<Map<String, Object>> settingList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(settingResDTOList))
        {
            Map<String, Object> mapTemp;
            for(WarningSettingResDTO item : settingResDTOList)
            {
                mapTemp = new HashMap<>();
                settingList.add(mapTemp);
                mapTemp.put(Field.WARNING_CODE, item.getWarningCode());
                mapTemp.put(Field.FILE_PATH, item.getFilePath());
                mapTemp.put(Field.PLAY, item.getPlay());
            }
        }
        dataMap.put(Field.SETTING_LIST, settingList);
        return dataMap;
    }

    private void cacheAllIndustryData()
    {
        ListDTO<DictIndustryResDTO> listDTO = systemRpcService.getDictIndustryInfoList();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(DictIndustryResDTO o : listDTO.getList())
            {
                redisDao.saveIndustryInfo(o.getId(), o.getName());
            }
        }
    }
}

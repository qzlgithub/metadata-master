package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.SistemDTO;
import com.mingdong.core.model.dto.request.ArticlesReqDTO;
import com.mingdong.core.model.dto.request.IndustryReqDTO;
import com.mingdong.core.model.dto.request.PrivilegeReqDTO;
import com.mingdong.core.model.dto.request.RechargeTypeReqDTO;
import com.mingdong.core.model.dto.request.SMSReqDTO;
import com.mingdong.core.model.dto.response.ArticlesDetailResDTO;
import com.mingdong.core.model.dto.response.ArticlesResDTO;
import com.mingdong.core.model.dto.response.DictIndustryResDTO;
import com.mingdong.core.model.dto.response.DictRechargeTypeResDTO;
import com.mingdong.core.model.dto.response.IndustryResDTO;
import com.mingdong.core.model.dto.response.PrivilegeResDTO;
import com.mingdong.core.model.dto.response.RechargeTypeResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.service.SystemRpcService;
import com.mingdong.mis.domain.entity.Articles;
import com.mingdong.mis.domain.entity.DictIndustry;
import com.mingdong.mis.domain.entity.DictRechargeType;
import com.mingdong.mis.domain.entity.Function;
import com.mingdong.mis.domain.mapper.ArticlesMapper;
import com.mingdong.mis.domain.mapper.DictIndustryMapper;
import com.mingdong.mis.domain.mapper.DictRechargeTypeMapper;
import com.mingdong.mis.domain.mapper.FunctionMapper;
import com.mingdong.mis.domain.mapper.SistemMapper;
import com.mingdong.mis.service.SMSService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SystemRpcServiceImpl implements SystemRpcService
{
    @Resource
    private DictIndustryMapper dictIndustryMapper;
    @Resource
    private FunctionMapper functionMapper;
    @Resource
    private DictRechargeTypeMapper dictRechargeTypeMapper;
    @Resource
    private SistemMapper sistemMapper;
    @Resource
    private ArticlesMapper articlesMapper;
    @Resource
    private SMSService smsService;

    @Override
    public ListDTO<DictIndustryResDTO> getIndustryList(Long parentIndustryId, Integer enabled)
    {
        ListDTO<DictIndustryResDTO> listDTO = new ListDTO<>();
        List<DictIndustry> dataList = dictIndustryMapper.getByParentAndStatus(parentIndustryId, enabled);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<DictIndustryResDTO> list = new ArrayList<>(dataList.size());
            for(DictIndustry o : dataList)
            {
                DictIndustryResDTO di = new DictIndustryResDTO();
                di.setId(o.getId());
                di.setCode(o.getCode());
                di.setName(o.getName());
                di.setEnabled(o.getEnabled());
                di.setParentId(o.getParentId());
                list.add(di);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<PrivilegeResDTO> getPrivilegeListByParent(Long parentId)
    {
        ListDTO<PrivilegeResDTO> listDTO = new ListDTO<>();
        if(parentId == null)
        {
            parentId = 0L;
        }
        List<Function> functionList = functionMapper.getListByParent(parentId);
        if(!CollectionUtils.isEmpty(functionList))
        {
            List<PrivilegeResDTO> list = new ArrayList<>(functionList.size());
            for(Function o : functionList)
            {
                PrivilegeResDTO p = new PrivilegeResDTO();
                p.setPrivilegeId(o.getId());
                p.setName(o.getName());
                p.setEnabled(o.getEnabled());
                list.add(p);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<PrivilegeResDTO> getPrivilegeByLevel(Integer level)
    {
        ListDTO<PrivilegeResDTO> listDTO = new ListDTO<>();
        List<Function> functionList = functionMapper.getListByLevel(level);
        if(!CollectionUtils.isEmpty(functionList))
        {
            List<PrivilegeResDTO> list = new ArrayList<>(functionList.size());
            for(Function o : functionList)
            {
                PrivilegeResDTO p = new PrivilegeResDTO();
                p.setPrivilegeId(o.getId());
                p.setName(o.getName());
                list.add(p);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<DictRechargeTypeResDTO> getRechargeTypeList(Integer enabled, Integer deleted)
    {
        ListDTO<DictRechargeTypeResDTO> listDTO = new ListDTO<>();
        List<DictRechargeType> rechargeTypeList = dictRechargeTypeMapper.getListByStatus(enabled, deleted);
        if(!CollectionUtils.isEmpty(rechargeTypeList))
        {
            List<DictRechargeTypeResDTO> list = new ArrayList<>();
            for(DictRechargeType o : rechargeTypeList)
            {
                DictRechargeTypeResDTO drt = new DictRechargeTypeResDTO();
                drt.setId(o.getId());
                drt.setName(o.getName());
                drt.setRemark(o.getRemark());
                drt.setEnabled(o.getEnabled());
                list.add(drt);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<DictIndustryResDTO> getDictIndustryInfoList()
    {
        ListDTO<DictIndustryResDTO> listDTO = new ListDTO<>();
        List<DictIndustry> dictIndustryList = dictIndustryMapper.getIndustryInfo();
        if(!CollectionUtils.isEmpty(dictIndustryList))
        {
            List<DictIndustryResDTO> list = new ArrayList<>(dictIndustryList.size());
            for(DictIndustry o : dictIndustryList)
            {
                DictIndustryResDTO di = new DictIndustryResDTO();
                di.setId(o.getId());
                di.setName(o.getName());
                list.add(di);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    @Transactional
    public void setModuleStatus(Integer status, List<Long> moduleIdList)
    {
        functionMapper.updateModuleStatusByIds(status, new Date(), moduleIdList);
    }

    @Override
    public IndustryResDTO getIndustryDictOfTarget(Long industryId)
    {
        IndustryResDTO dto = new IndustryResDTO();
        DictIndustry self = dictIndustryMapper.findById(industryId);
        if(self == null)
        {
            dto.setResult(RestResult.OBJECT_NOT_FOUND);
            return dto;
        }
        dto.setParentId(self.getParentId());
        List<DictIndustry> peerList = dictIndustryMapper.getByParentAndStatus(self.getParentId(), TrueOrFalse.TRUE);
        List<Dict> peers = new ArrayList<>();
        for(DictIndustry d : peerList)
        {
            peers.add(new Dict(d.getId() + "", d.getName()));
        }
        dto.setPeers(peers);
        List<DictIndustry> parentList = dictIndustryMapper.getByParentAndStatus(0L, TrueOrFalse.TRUE);
        List<Dict> parents = new ArrayList<>();
        for(DictIndustry d : parentList)
        {
            parents.add(new Dict(d.getId() + "", d.getName()));
        }
        dto.setParents(parents);
        return dto;
    }

    @Override
    @Transactional
    public ResponseDTO addIndustryType(IndustryReqDTO reqDTO)
    {
        ResponseDTO respDTO = new ResponseDTO();
        DictIndustry industry = dictIndustryMapper.findByCode(reqDTO.getCode());
        if(industry != null)
        {
            respDTO.setResult(RestResult.INDUSTRY_CODE_EXIST);
            return respDTO;
        }
        Date date = new Date();
        industry = new DictIndustry();
        industry.setCreateTime(date);
        industry.setUpdateTime(date);
        industry.setCode(reqDTO.getCode());
        industry.setName(reqDTO.getName());
        industry.setParentId(reqDTO.getParentId());
        industry.setSeqNo(1); // TODO 行业序号
        industry.setEnabled(TrueOrFalse.TRUE);
        dictIndustryMapper.add(industry);
        return respDTO;
    }

    @Override
    @Transactional
    public ResponseDTO editIndustryInfo(IndustryReqDTO reqDTO)
    {
        ResponseDTO res = new ResponseDTO();
        DictIndustry industry = dictIndustryMapper.findById(reqDTO.getId());
        if(industry == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        industry = dictIndustryMapper.findByCode(reqDTO.getCode());
        if(industry != null && !industry.getId().equals(reqDTO.getId()))
        {
            res.setResult(RestResult.INDUSTRY_CODE_EXIST);
            return res;
        }
        DictIndustry temp = new DictIndustry();
        temp.setId(reqDTO.getId());
        temp.setUpdateTime(new Date());
        temp.setCode(reqDTO.getCode());
        temp.setName(reqDTO.getName());
        dictIndustryMapper.updateSkipNull(temp);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO editPrivilegeInfo(PrivilegeReqDTO reqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Function function = functionMapper.findById(reqDTO.getPrivilegeId());
        if(function == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        Function temp = new Function();
        temp.setId(reqDTO.getPrivilegeId());
        temp.setUpdateTime(new Date());
        temp.setName(reqDTO.getName());
        functionMapper.updateSkipNull(temp);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addRechargeType(String name, String remark)
    {
        Date current = new Date();
        ResponseDTO res = new ResponseDTO();
        DictRechargeType rechargeType = dictRechargeTypeMapper.findByName(name);
        if(rechargeType != null && TrueOrFalse.FALSE.equals(rechargeType.getDeleted()))
        {
            res.setResult(RestResult.CATEGORY_NAME_EXIST);
            return res;
        }
        if(rechargeType == null)
        {
            rechargeType = new DictRechargeType();
            rechargeType.setCreateTime(current);
            rechargeType.setUpdateTime(current);
            rechargeType.setName(name);
            rechargeType.setRemark(remark);
            rechargeType.setEnabled(TrueOrFalse.TRUE);
            rechargeType.setDeleted(TrueOrFalse.FALSE);
            dictRechargeTypeMapper.add(rechargeType);
        }
        else
        {
            rechargeType.setUpdateTime(current);
            rechargeType.setRemark(remark);
            rechargeType.setEnabled(TrueOrFalse.TRUE);
            rechargeType.setDeleted(TrueOrFalse.FALSE);
            dictRechargeTypeMapper.updateById(rechargeType);
        }
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO changeIndustryStatus(Long industryTypeId, Integer enabled)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        DictIndustry byId = dictIndustryMapper.findById(industryTypeId);
        if(byId != null && !enabled.equals(byId.getEnabled()))
        {
            DictIndustry updObj = new DictIndustry();
            updObj.setId(industryTypeId);
            updObj.setUpdateTime(new Date());
            updObj.setEnabled(enabled);
            dictIndustryMapper.updateSkipNull(updObj);
        }
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
    }

    @Override
    public ListDTO<RechargeTypeResDTO> getRechargeList()
    {
        ListDTO<RechargeTypeResDTO> res = new ListDTO<>();
        List<DictRechargeType> dataList = dictRechargeTypeMapper.getAvailableList();
        res.setTotal(dataList.size());
        List<RechargeTypeResDTO> list = new ArrayList<>();
        for(DictRechargeType o : dataList)
        {
            RechargeTypeResDTO dto = new RechargeTypeResDTO();
            dto.setId(o.getId());
            dto.setName(o.getName());
            dto.setRemark(o.getRemark());
            dto.setEnabled(o.getEnabled());
            dto.setAddAt(o.getCreateTime());
            list.add(dto);
        }
        res.setList(list);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO editRechargeType(RechargeTypeReqDTO reqDTO)
    {
        ResponseDTO res = new ResponseDTO();
        DictRechargeType objUpd = dictRechargeTypeMapper.findById(reqDTO.getId());
        if(objUpd == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        if(!StringUtils.isNullBlank(reqDTO.getName()))
        {
            DictRechargeType temp = dictRechargeTypeMapper.findByName(reqDTO.getName());
            if(temp != null && !temp.getId().equals(objUpd.getId()))
            {
                res.setResult(RestResult.CATEGORY_NAME_EXIST);
                return res;
            }
            objUpd.setName(reqDTO.getName());
            objUpd.setRemark(reqDTO.getRemark());
        }
        if(TrueOrFalse.TRUE.equals(reqDTO.getEnabled()) || TrueOrFalse.FALSE.equals(reqDTO.getEnabled()))
        {
            objUpd.setEnabled(reqDTO.getEnabled());
        }
        objUpd.setUpdateTime(new Date());
        dictRechargeTypeMapper.updateById(objUpd);
        return res;
    }

    @Override
    public SistemDTO getSystemSetting()
    {
        SistemDTO sistemDTO = new SistemDTO();
        String clientUserMax = sistemMapper.getClientUserMax();
        String serviceQQ = sistemMapper.getServiceQQ();
        sistemDTO.setClientUserMax(Integer.valueOf(clientUserMax));
        sistemDTO.setServiceQQ(serviceQQ);
        return sistemDTO;
    }

    @Override
    @Transactional
    public void editSystemSetting(SistemDTO sistemDTO)
    {
        Date date = new Date();
        sistemMapper.updateClientUserMax(sistemDTO.getClientUserMax() + "", date);
        sistemMapper.updateServiceQQ(sistemDTO.getServiceQQ(), date);
    }

    @Override
    public ListDTO<ArticlesResDTO> getArticlesList(Page page)
    {
        ListDTO<ArticlesResDTO> listDTO = new ListDTO<>();
        int total = articlesMapper.countAll();
        long pages = page.getPages(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<Articles> dataList = articlesMapper.findListAll();
            List<ArticlesResDTO> list = new ArrayList<>(dataList.size());
            for(Articles o : dataList)
            {
                ArticlesResDTO pd = new ArticlesResDTO();
                pd.setId(o.getId());
                pd.setPublishTime(o.getPublishTime());
                pd.setPublished(o.getPublished());
                pd.setOrderId(o.getOrderId());
                pd.setTitle(o.getTitle());
                pd.setType(o.getType());
                list.add(pd);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addArticles(ArticlesReqDTO articlesReqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Articles articles = new Articles();
        articles.setId(articlesReqDTO.getId());
        articles.setAuthor(articlesReqDTO.getAuthor());
        articles.setContent(articlesReqDTO.getContent());
        articles.setCreateTime(new Date());
        articles.setUpdateTime(new Date());
        articles.setDeleted(articlesReqDTO.getDeleted());
        articles.setPublished(articlesReqDTO.getPublished());
        articles.setImagePath(articlesReqDTO.getImagePath());
        articles.setOrderId(articlesReqDTO.getOrderId());
        articles.setSynopsis(articlesReqDTO.getSynopsis());
        articles.setTitle(articlesReqDTO.getTitle());
        articles.setType(articlesReqDTO.getType());
        articles.setUserId(articlesReqDTO.getUserId());
        articles.setPublishTime(articlesReqDTO.getPublishTime());
        articlesMapper.add(articles);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateArticles(ArticlesReqDTO articlesReqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Articles articles = new Articles();
        articles.setId(articlesReqDTO.getId());
        articles.setAuthor(articlesReqDTO.getAuthor());
        articles.setContent(articlesReqDTO.getContent());
        articles.setUpdateTime(new Date());
        articles.setDeleted(articlesReqDTO.getDeleted());
        articles.setPublished(articlesReqDTO.getPublished());
        articles.setImagePath(articlesReqDTO.getImagePath());
        articles.setOrderId(articlesReqDTO.getOrderId());
        articles.setSynopsis(articlesReqDTO.getSynopsis());
        articles.setTitle(articlesReqDTO.getTitle());
        articles.setType(articlesReqDTO.getType());
        articles.setUserId(articlesReqDTO.getUserId());
        articles.setPublishTime(articlesReqDTO.getPublishTime());
        articlesMapper.updateSkipNull(articles);
        return responseDTO;
    }

    @Override
    public ArticlesDetailResDTO getArticlesInfo(Long id)
    {
        ArticlesDetailResDTO articlesResDTO = new ArticlesDetailResDTO();
        Articles articles = articlesMapper.findById(id);
        articlesResDTO.setId(articles.getId());
        articlesResDTO.setUserId(articles.getUserId());
        articlesResDTO.setType(articles.getType());
        articlesResDTO.setTitle(articles.getTitle());
        articlesResDTO.setSynopsis(articles.getSynopsis());
        articlesResDTO.setOrderId(articles.getOrderId());
        articlesResDTO.setImagePath(articles.getImagePath());
        articlesResDTO.setPublished(articles.getPublished());
        articlesResDTO.setDeleted(articles.getDeleted());
        articlesResDTO.setContent(articles.getContent());
        articlesResDTO.setAuthor(articles.getAuthor());
        articlesResDTO.setPublishTime(articles.getPublishTime());
        return articlesResDTO;
    }

    @Override
    public ResponseDTO deleteArticlesById(Long id)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Articles articles = new Articles();
        articles.setId(id);
        articles.setDeleted(TrueOrFalse.TRUE);
        articlesMapper.updateSkipNull(articles);
        return responseDTO;
    }

    @Override
    public ListDTO<ArticlesResDTO> getArticlesList(Integer type, Page page)
    {
        ListDTO<ArticlesResDTO> listDTO = new ListDTO<>();
        int total = articlesMapper.countByType(type);
        long pages = page.getPages(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<Articles> dataList = articlesMapper.findListByType(type);
            List<ArticlesResDTO> list = new ArrayList<>(dataList.size());
            for(Articles o : dataList)
            {
                ArticlesResDTO pd = new ArticlesResDTO();
                pd.setId(o.getId());
                pd.setType(o.getType());
                pd.setPublishTime(o.getPublishTime());
                pd.setTitle(o.getTitle());
                pd.setSynopsis(o.getSynopsis());
                pd.setImagePath(o.getImagePath());
                list.add(pd);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public void sendSMS(List<SMSReqDTO> smsList)
    {
        new Thread(() -> {
            if(!CollectionUtils.isEmpty(smsList))
            {
                for(SMSReqDTO item : smsList)
                {
                    try
                    {
                        smsService.sendSMS(item.getSmsType(), item.getContent(), item.getPhone());
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}

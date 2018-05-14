package com.mingdong.core.service;

import com.mingdong.common.model.Page;
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

import java.util.List;

public interface SystemRpcService
{
    /**
     * 根据条件获取行业分类信息列表
     */
    ListDTO<DictIndustryResDTO> getIndustryList(Long parentIndustryId, Integer enabled);

    /**
     * 查询指定父级的子级功能列表
     */
    ListDTO<PrivilegeResDTO> getPrivilegeListByParent(Long parentId);

    /**
     * 根据菜单功能等级获取功能列表
     */
    ListDTO<PrivilegeResDTO> getPrivilegeByLevel(Integer level);

    /**
     * 根据状态获取充值类型列表
     */
    ListDTO<DictRechargeTypeResDTO> getRechargeTypeList(Integer enabled, Integer deleted);

    /**
     * 获取行业分类列表
     */
    ListDTO<DictIndustryResDTO> getDictIndustryInfoList();

    /**
     * 修改模块状态
     */
    void setModuleStatus(Integer status, List<Long> moduleIdList);

    IndustryResDTO getIndustryDictOfTarget(Long industryId);

    /**
     * 新增行业分类，判断code
     */
    ResponseDTO addIndustryType(IndustryReqDTO reqDTO);

    /**
     * 修改行业分类，判断code
     */
    ResponseDTO editIndustryInfo(IndustryReqDTO reqDTO);

    /**
     * 修改权限信息
     */
    ResponseDTO editPrivilegeInfo(PrivilegeReqDTO reqDTO);

    /**
     * 新增or修改充值类型备注
     */
    ResponseDTO addRechargeType(String name, String remark);

    /**
     * 修改行业分类状态
     */
    ResponseDTO changeIndustryStatus(Long industryTypeId, Integer enabled);

    /**
     * 查询充值类型列表
     */
    ListDTO<RechargeTypeResDTO> getRechargeList();

    /**
     * 根据ID编辑充值类型
     */
    ResponseDTO editRechargeType(RechargeTypeReqDTO reqDTO);

    /**
     * 获取系统参数
     */
    SistemDTO getSystemSetting();

    /**
     * 编辑系统参数
     */
    void editSystemSetting(SistemDTO sistemDTO);

    ListDTO<ArticlesResDTO> getArticlesList(Page page);

    ResponseDTO addArticles(ArticlesReqDTO articlesReqDTO);

    ResponseDTO updateArticles(ArticlesReqDTO articlesReqDTO);

    ArticlesDetailResDTO getArticlesInfo(Long id);

    ResponseDTO deleteArticlesById(Long id);

    ListDTO<ArticlesResDTO> getArticlesList(Integer type, Page page);

    void sendSMS(List<SMSReqDTO> smsList);
}

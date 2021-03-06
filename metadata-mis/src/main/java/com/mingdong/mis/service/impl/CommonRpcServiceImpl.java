package com.mingdong.mis.service.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.DictIndustry;
import com.mingdong.mis.domain.entity.DictRechargeType;
import com.mingdong.mis.domain.entity.Group;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.ProductClientInfo;
import com.mingdong.mis.domain.entity.User;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.DictIndustryMapper;
import com.mingdong.mis.domain.mapper.DictRechargeTypeMapper;
import com.mingdong.mis.domain.mapper.GroupMapper;
import com.mingdong.mis.domain.mapper.ProductClientInfoMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.domain.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class CommonRpcServiceImpl implements CommonRpcService
{
    @Resource
    private UserMapper userMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ClientUserMapper clientUserMapper;
    @Resource
    private DictIndustryMapper dictIndustryMapper;
    @Resource
    private DictRechargeTypeMapper dictRechargeTypeMapper;
    @Resource
    private ProductClientInfoMapper productClientInfoMapper;

    @Override
    public Integer checkIfClientExist(String username)
    {
        ClientUser user = clientUserMapper.findByUsername(username);
        return user != null ? TrueOrFalse.TRUE : TrueOrFalse.FALSE;
    }

    @Override
    public Integer checkIfGroupExist(String name)
    {
        Group role = groupMapper.findByName(name);
        return role != null ? TrueOrFalse.TRUE : TrueOrFalse.FALSE;
    }

    @Override
    public Integer checkIfIndustryExist(String code)
    {
        DictIndustry o = dictIndustryMapper.findByCode(code);
        return o != null ? TrueOrFalse.TRUE : TrueOrFalse.FALSE;
    }

    @Override
    public ListDTO<Dict> getProductDict()
    {
        ListDTO<Dict> listDTO = new ListDTO<>();
        List<Product> dataList = productMapper.getListByStatus(TrueOrFalse.TRUE);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<Dict> list = new ArrayList<>(dataList.size());
            for(Product o : dataList)
            {
                list.add(new Dict(o.getId() + "", o.getName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<Dict> getAdminGroupDict()
    {
        ListDTO<Dict> listDTO = new ListDTO<>();
        List<Group> roleList = groupMapper.getByStatus(TrueOrFalse.TRUE);
        if(!CollectionUtils.isEmpty(roleList))
        {
            List<Dict> list = new ArrayList<>(roleList.size());
            for(Group o : roleList)
            {
                list.add(new Dict(o.getId() + "", o.getName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<Dict> getAdminUserDict()
    {
        ListDTO<Dict> listDTO = new ListDTO<>();
        List<User> userList = userMapper.getListBy(null, TrueOrFalse.TRUE);
        if(!CollectionUtils.isEmpty(userList))
        {
            List<Dict> list = new ArrayList<>();
            for(User o : userList)
            {
                list.add(new Dict(o.getId() + "", o.getName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<Dict> getClientProductDict(Long clientId)
    {
        ListDTO<Dict> listDTO = new ListDTO<>();
        List<ProductClientInfo> dataList = productClientInfoMapper.getClientDictList(clientId);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<Dict> list = new ArrayList<>();
            for(ProductClientInfo o : dataList)
            {
                list.add(new Dict(o.getProductId() + "", o.getProductName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<Dict> getRechargeTypeDict()
    {
        ListDTO<Dict> listDTO = new ListDTO<>();
        List<DictRechargeType> rechargeTypeList = dictRechargeTypeMapper.getListByStatus(TrueOrFalse.TRUE,
                TrueOrFalse.FALSE);
        if(!CollectionUtils.isEmpty(rechargeTypeList))
        {
            List<Dict> list = new ArrayList<>();
            for(DictRechargeType o : rechargeTypeList)
            {
                list.add(new Dict(o.getId() + "", o.getName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }
}

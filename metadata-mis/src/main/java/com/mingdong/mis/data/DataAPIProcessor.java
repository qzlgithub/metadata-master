package com.mingdong.mis.data;

import com.mingdong.core.exception.MetadataAPIException;
import com.mingdong.mis.constant.APIProduct;
import com.mingdong.mis.data.service.DSDataAPI;
import com.mingdong.mis.model.IMetadata;
import com.mingdong.mis.model.vo.AbstractAccessBean;
import com.mingdong.mis.model.vo.BlacklistVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DataAPIProcessor
{
    @Resource
    private DSDataAPI dsDataAPI;

    public IMetadata revokeDataAPI(String code, AbstractAccessBean bean) throws MetadataAPIException
    {
        APIProduct product = APIProduct.getByCode(code);
        switch(product)
        {
            case DS_DATA_BLACKLIST:
                BlacklistVO req = (BlacklistVO) bean;
                return dsDataAPI.callBlacklist(req.getIdNo(), req.getName(), req.getPhone());

        }
        throw new MetadataAPIException("Invalid product code: " + code);
    }
}

package com.mingdong.mis.data;

import com.mingdong.core.exception.MetadataAPIException;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.mis.constant.APIProduct;
import com.mingdong.mis.data.service.DSDataAPI;
import com.mingdong.mis.model.IMetadata;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.model.vo.BlacklistVO;
import com.mingdong.mis.model.vo.MultipleAppVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DataAPIProcessor
{
    @Resource
    private DSDataAPI dsDataAPI;

    public IMetadata revokeDataAPI(APIProduct product, AbsPayload bean)
            throws MetadataAPIException, MetadataCoreException
    {
        switch(product)
        {
            case DS_DATA_BLACKLIST:
                BlacklistVO blacklistVO = (BlacklistVO) bean;
                return dsDataAPI.callBlacklist(blacklistVO);
            case DS_DATA_MULTI_APP:
                MultipleAppVO multipleAppVO = (MultipleAppVO) bean;
                return dsDataAPI.callMultipleApp(multipleAppVO);
        }
        throw new MetadataAPIException("Invalid product: " + product);
    }
}

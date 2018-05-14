package com.mingdong.mis.processor;

import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.vo.AbsPayload;

public interface IProcessor<T extends AbsPayload>
{
    String DEFAULT_PERSON_ID = "0";

    Metadata process(T payload) throws Exception;
}

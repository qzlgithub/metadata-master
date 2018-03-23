package com.mingdong.mis.processor;

import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.vo.AbsPayload;

public interface IProcessor<T extends AbsPayload>
{
    Metadata process(T payload);
}

package com.mingdong.mis.model.metadata;

import com.mingdong.mis.model.IMetadata;

import java.util.Map;

public class Blacklist implements IMetadata
{

    @Override
    public boolean isHit()
    {
        return false;
    }

    @Override
    public Map<String, Object> response()
    {
        return null;
    }
}

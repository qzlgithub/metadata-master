package com.mingdong.core.model.dto;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;

import java.util.Arrays;

public class SysConfigDTO extends BaseDTO
{
    private Long id;
    private String name;
    private String value;

    public SysConfigDTO(){

    }

    public SysConfigDTO(RestResult result){
        super(result);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Integer getIntegerValue(){
        return StringUtils.isNullBlank(value)?null:Integer.valueOf(value);
    }

    public String getStringValue(){
        return value;
    }

    public Boolean getBooleanValue(){
        if(StringUtils.isNullBlank(value)){
            return null;
        }else{
            if(Arrays.asList(new String[]{"0","1"}).contains(value)){
                if("0".equals(value)){
                    return false;
                }else{
                    return true;
                }
            }else{
                return Boolean.valueOf(value);
            }
        }
    }

}

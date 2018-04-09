package com.mingdong.core.model.dto.request;

public class JobLogReqDTO
{
    private String jobCode;
    private Integer success;
    private String remark;

    public String getJobCode()
    {
        return jobCode;
    }

    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode;
    }

    public Integer getSuccess()
    {
        return success;
    }

    public void setSuccess(Integer success)
    {
        this.success = success;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}

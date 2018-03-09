package com.mingdong.core.model.dto.request;

import com.mingdong.core.model.dto.RequestDTO;

import java.util.Date;

public class IntervalReqDTO extends RequestDTO
{
    private Date startTime;
    private Date endTime;

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }
}

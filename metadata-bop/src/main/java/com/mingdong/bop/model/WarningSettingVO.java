package com.mingdong.bop.model;

public class WarningSettingVO
{
    private Long id;
    private int send;
    private int play;
    private Integer generalLimit;
    private Integer severityLimit;
    private Integer warningLimit;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getSend()
    {
        return send;
    }

    public void setSend(Integer send)
    {
        this.send = send;
    }

    public Integer getPlay()
    {
        return play;
    }

    public void setPlay(Integer play)
    {
        this.play = play;
    }

    public Integer getGeneralLimit()
    {
        return generalLimit;
    }

    public void setGeneralLimit(Integer generalLimit)
    {
        this.generalLimit = generalLimit;
    }

    public Integer getSeverityLimit()
    {
        return severityLimit;
    }

    public void setSeverityLimit(Integer severityLimit)
    {
        this.severityLimit = severityLimit;
    }

    public Integer getWarningLimit()
    {
        return warningLimit;
    }

    public void setWarningLimit(Integer warningLimit)
    {
        this.warningLimit = warningLimit;
    }
}

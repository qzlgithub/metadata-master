package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.List;

public class ListDTO<T> implements Serializable
{
    private int total;
    private List<T> list;

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public List<T> getList()
    {
        return list;
    }

    public void setList(List<T> list)
    {
        this.list = list;
    }
}

package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class MessageListDTO implements Serializable
{
    private List<MessageDTO> messages;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public MessageListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public void setResultDTO(ResultDTO resultDTO)
    {
        this.resultDTO = resultDTO;
    }

    public List<MessageDTO> getMessages()
    {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages)
    {
        this.messages = messages;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public int getPages()
    {
        return pages;
    }

    public void setPages(int pages)
    {
        this.pages = pages;
    }
}

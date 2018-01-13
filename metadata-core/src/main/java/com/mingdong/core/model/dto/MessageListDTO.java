package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.util.List;

public class MessageListDTO extends BaseDTO
{
    private List<MessageDTO> messages;
    private int total;
    private int pages;

    public MessageListDTO(RestResult result)
    {
        super(result);
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

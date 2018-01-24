package com.mingdong.bop.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
public class Param
{
    @Value("${node.id}")
    private int nodeId;

    public int getNodeId()
    {
        return nodeId;
    }
}

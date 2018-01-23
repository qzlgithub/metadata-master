package com.mingdong.core.util;

import java.util.concurrent.atomic.AtomicLong;

public class IDUtils
{
    private static AtomicLong product = new AtomicLong(0);
    private static AtomicLong client = new AtomicLong(0);
    private static AtomicLong clientUser = new AtomicLong(0);
    private static AtomicLong manager = new AtomicLong(0);
    private static AtomicLong role = new AtomicLong(0);
    private static AtomicLong clientProduct = new AtomicLong(0);
    private static AtomicLong productRecharge = new AtomicLong(0);

    private static long createID(AtomicLong atomicLong, int nodeId)
    {
        long time = System.currentTimeMillis() - 1483200000000L; // "2017-01-01 00:00:00"
        int incNum = (int) (atomicLong.incrementAndGet() % 65535);
        long id = time << 7;
        id = id ^ nodeId;
        id = id << 16;
        id = id ^ incNum;
        return id;
    }

    public static Long getProductId(int nodeId)
    {
        return createID(product, nodeId);
    }

    public static Long getClientId(int nodeId)
    {
        return createID(client, nodeId);
    }

    public static Long getClientUser(int nodeId)
    {
        return createID(clientUser, nodeId);
    }

    public static Long getManagerId(int nodeId)
    {
        return createID(manager, nodeId);
    }

    public static Long getRoleId(int nodeId)
    {
        return createID(role, nodeId);
    }

    public static Long getClientProductId(int nodeId)
    {
        return createID(clientProduct, nodeId);
    }

    public static Long getProductRechargeId(int nodeId)
    {
        return createID(productRecharge, nodeId);
    }
}

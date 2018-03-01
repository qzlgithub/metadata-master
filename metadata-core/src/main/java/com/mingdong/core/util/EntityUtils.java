package com.mingdong.core.util;

import org.springframework.cglib.beans.BeanCopier;

@Deprecated
public class EntityUtils
{
    /**
     * 复制对象属性
     */
    public static <T, K> K copyProperties(T source, K target)
    {
        if(source == null)
        {
            return target;
        }
        if(target == null)
        {
            return null;
        }

        BeanCopier bc = BeanCopier.create(source.getClass(), target.getClass(), false);
        try
        {
            bc.copy(source, target, null);
            return target;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}

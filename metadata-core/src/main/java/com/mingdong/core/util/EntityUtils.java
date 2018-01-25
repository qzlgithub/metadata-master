package com.mingdong.core.util;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils
{

    /**
     * 类属性复制
     *
     * @param source
     * @param target
     * @param ignoreNull ，如果为true，则将源对象中属性为null的值不进行复制，
     *                   如果是整个对象属性一致的，则调用其他copyProperties方法，以提高效率
     * @return
     */
    public static <T, K> K copyProperties(T source, K target, boolean ignoreNull)
    {
        if(source == null)
        {
            return target;
        }
        if(target == null)
        {
            return null;
        }

        if(!ignoreNull)
        {
            return copyProperties(source, target);
        }
        else
        {
            BeanMap map = BeanMap.create(BeanUtils.instantiateClass(target.getClass()));
            map.setBean(source);

            List<String> getters = new ArrayList<String>();

            for(Object key : map.keySet())
            {
                Object v = map.get(key);
                if(v == null)
                {
                    getters.add(key.toString());
                }
            }
            BeanUtils.copyProperties(source, target, getters.toArray(new String[0]));
            return target;
        }
    }

    /**
     * 复制对象属性
     *
     * @param source
     * @param target
     * @return
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

    /**
     * 复制对象属性
     *
     * @param source
     * @param targetClass
     * @return
     */
    public static <T, K> K copyProperties(T source, Class<K> targetClass)
    {
        if(source == null)
        {
            return null;
        }
        BeanCopier bc = BeanCopier.create(source.getClass(), targetClass, false);
        try
        {
            K k = BeanUtils.instantiateClass(targetClass);
            bc.copy(source, k, null);
            return k;
        }
        catch(Exception e)
        {
            return null;
        }
    }

}

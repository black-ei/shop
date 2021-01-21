package com.wangyi.shop.utils;

import org.springframework.beans.BeanUtils;

public class CopyBean {

    public static <T> T copyProperties(Object o,Class<T> clazz){
        if(null==o) return null;
        if (null==clazz) return null;
        try {
            T t = clazz.newInstance();
            BeanUtils.copyProperties(o,t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

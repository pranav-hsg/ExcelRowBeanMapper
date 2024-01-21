package com.poimapper.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

@FunctionalInterface
public interface CastString {
    Object apply(Field field, String value, Map<String,Object> options) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
}

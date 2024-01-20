package com.poimapper.config;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface CastString {
    Object apply(Class<?> fieldType, String value) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
}

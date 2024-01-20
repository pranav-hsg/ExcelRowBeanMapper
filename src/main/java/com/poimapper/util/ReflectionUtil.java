package com.poimapper.util;

import java.lang.reflect.Method;

public class ReflectionUtil {
    public static Method getSetterMethod(Class<?> clazz, String fieldName, Class<?> fieldType) throws NoSuchMethodException {
        String setterMethodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return clazz.getMethod(setterMethodName, fieldType);
    }
    public static Method getGetterMethod(Class<?> clazz, String fieldName) throws NoSuchMethodException {
        String getterMethodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return clazz.getMethod(getterMethodName);
    }
}

package com.poimapper.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DefaultCastString implements  CastString{

    public Object apply(Class<?> fieldType, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            if (fieldType == String.class) {
                return value;
            } else if (fieldType == Integer.class || fieldType == int.class) {
                return Integer.parseInt(value);
            } else if (fieldType == Double.class || fieldType == double.class) {
                return Double.parseDouble(value);
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (fieldType == LocalDate.class) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CastConstants.DATE_PATTERN);
                return LocalDate.parse(value, formatter);
            } else if (fieldType == ZonedDateTime.class) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CastConstants.DATE_TIME_PATTERN);
                return ZonedDateTime.parse(value, formatter);
            } else if (fieldType == BigDecimal.class) {
                return new BigDecimal(value);
            } else if (Enum.class.isAssignableFrom(fieldType)) {
                Method valueOf = fieldType.getMethod("valueOf", String.class);
                return valueOf.invoke(fieldType, value);
            }
        }catch( RuntimeException e){
            new RuntimeException(e);
        }
        return null;
    }
}


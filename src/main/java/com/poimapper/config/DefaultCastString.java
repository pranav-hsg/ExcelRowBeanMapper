package com.poimapper.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DefaultCastString implements  CastString{
    ExcelRowBeanMapperOptions defaultOptions =ExcelRowBeanMapperOptions.getInstance();
    public Object apply(Field field, String value, Map<String,String> options) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> fieldType = field.getType();
        String datePattern = options.get("pattern")!=null ? options.get("pattern") :  defaultOptions.getDatePattern();
        String dateTimePattern = options.get("pattern") != null ? options.get("pattern") : defaultOptions.getDateTimePattern();
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
                return LocalDate.parse(value, formatter);
            } else if (fieldType == LocalDateTime.class) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
                return LocalDateTime.parse(value, formatter);
            }else if (fieldType == ZonedDateTime.class) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
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


package com.poimapper.config;

import com.poimapper.constants.ErrorCodes;
import com.poimapper.exception.CastException;
import com.poimapper.util.ErrorMessageGenerationUtil;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.logging.log4j.spi.LoggerContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Logger;

public class DefaultCastString implements  CastString{
    private static final Logger logger = Logger.getLogger(DefaultCastString.class.getName());
    ExcelRowBeanMapperOptions defaultOptions =ExcelRowBeanMapperOptions.getInstance();
    public Object apply(Field field, String value, Map<String,Object> options) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> fieldType = field.getType();
        String datePattern = options.get("pattern")!=null ? (String)options.get("pattern") :  defaultOptions.getDatePattern();
        String dateTimePattern = options.get("pattern") != null ? (String)options.get("pattern") : defaultOptions.getDateTimePattern();
        Object defaultValue = options.getOrDefault("defaultValue",null);
        if( (value==null || value.trim().isEmpty()) && defaultValue != null)
            return defaultValue;
        if(value == null)
            return null;
        try {
            if (fieldType == String.class) {
                return value;
            } else if (fieldType == Integer.class || fieldType == int.class) {
                return Integer.parseInt(value);
            } else if (fieldType == Double.class || fieldType == double.class) {
                return Double.parseDouble(value);
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                if ("1".equals(value.trim()))
                    return true;
                if ("0".equals(value.trim()))
                    return false;
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
            }else if (fieldType == Number.class){
                return NumberFormat.getInstance().parse(value);
            }
        }catch(RuntimeException | ParseException e){
            String message = ErrorMessageGenerationUtil.getErrorMessage(ErrorCodes.VALUE_CONVERSION_FAILED,field.getName(),value,fieldType);
            if((boolean)defaultOptions.getMapperSettings().getOrDefault("strictMode",false))
                throw new CastException(message,e);
            logger.warning("Skipping field '"+field.getName()+"'\tFailed to cast value '"+value+"' from String type to '"+fieldType+"' "+e.getLocalizedMessage());
        }
        return null;
    }
}


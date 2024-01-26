package com.poimapper;

import com.poimapper.config.CastString;
import com.poimapper.config.DefaultCastString;
import com.poimapper.config.ExcelRowBeanMapperOptions;
import com.poimapper.exception.ExcelRowBeanMapperException;
import com.poimapper.exception.MissingConfigurationException;
import com.poimapper.util.ErrorMessageGenerationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.poimapper.constants.ErrorCodes.*;
@Slf4j
public class ExcelRowBeanMapper {

    private ExcelRowBeanMapper(){

    }
    private ExcelRowBeanMapper(Builder builder){
        rowMapping = builder.options.getRowMappingOptions();
        customStringCastingFunc = builder.options.getCustomCastingFunc()!= null ?builder.options.getCustomCastingFunc() :  new DefaultCastString();
        mapperSettings = builder.options.getMapperSettings() !=null ? builder.options.getMapperSettings() : new HashMap<>();
    }
    private  LinkedHashMap<String, Map<String,Object>> rowMapping = null;

    private Map<String,Object> mapperSettings;
    private  CastString customStringCastingFunc;

    private final DataFormatter dataFormatter = new DataFormatter();
    public  <T> T fromExcelRow(Row row, T dto)  {
        if(row == null)
            throw new ExcelRowBeanMapperException(ErrorMessageGenerationUtil.getErrorMessage(EMPTY_ROW_EXCEPTION));
        int i = 0;
        for (Map.Entry<String, Map<String,Object>> entry : rowMapping.entrySet()) {
            String cellValue = dataFormatter.formatCellValue(row.getCell(i));
            String fieldMapName = (String) entry.getValue().get("fieldMapping");
            try{
                setNestedValue(dto,fieldMapName,cellValue,entry.getValue());
            }catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                if((boolean)mapperSettings.getOrDefault("strictMode",false))
                    throw new ExcelRowBeanMapperException(ErrorMessageGenerationUtil.getErrorMessage(VALUE_HANDLE_EXCEPTION,fieldMapName), e);
                if(!(boolean)mapperSettings.getOrDefault("suppressWarnings",true))
                    log.warn(ErrorMessageGenerationUtil.getErrorMessage(VALUE_HANDLE_EXCEPTION,fieldMapName), e);
            }
            i++;
        }
        return dto;
    }
    private  <T> void setNestedValue(T dto, String fieldMapName, String cellValue,Map<String,Object> options) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Object currentClassInstance = dto;
        List<String> fields = new LinkedList<>(List.of(fieldMapName.split(":")));
        while (true){
            String curFieldMapName = fields.remove(0);
            Field curField =  currentClassInstance.getClass().getDeclaredField(curFieldMapName);
            curField.setAccessible(true);
            if(fields.isEmpty()) {
                // For non nested Fields set the value by casting string to particular data type of the field
                curField.set(currentClassInstance, customStringCastingFunc.apply(curField,cellValue,options) );
                break;
            }
            Object getterValue = curField.get(currentClassInstance);
            Object nestedClassInstance = (getterValue == null) ?  curField.getType().getConstructor().newInstance() : getterValue;
            curField.set(currentClassInstance,nestedClassInstance);
            currentClassInstance =  nestedClassInstance;
        }
    }
    public static class Builder {
        ExcelRowBeanMapperOptions options = ExcelRowBeanMapperOptions.getInstance();

        public Builder setRowMapping(LinkedHashMap<String, Map<String, Object>> rowMapping) {
            options.setRowMappingOptions(rowMapping);
            return this;
        }
        public Builder setCustomStringCastingFunc(CastString customStringCastingFunc) {
            options.setCustomCastingFunc(customStringCastingFunc);
            return this;
        }

        public Builder setDateFormat(String format){
            options.setDatePattern(format);
            return this;
        }
        public Builder setDateTimeFormat(String format){
            options.setDateTimePattern(format);
            return this;
        }
        public Builder setMapperSettings(Map<String,Object> mapperSettings){
            if(mapperSettings!=null)
                options.setMapperSettings(mapperSettings);
            return this;
        }
        public ExcelRowBeanMapper build() {
            if(options.getRowMappingOptions() == null)
                throw  new MissingConfigurationException(ErrorMessageGenerationUtil.getErrorMessage(MISSING_CONFIGURATION,ExcelRowBeanMapper.class.getName(),"options"));
            return new ExcelRowBeanMapper(this);
        }
    }
}

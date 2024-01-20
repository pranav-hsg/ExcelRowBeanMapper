package com.poimapper;

import com.poimapper.config.CastString;
import com.poimapper.config.DefaultCastString;
import com.poimapper.config.ExcelRowBeanMapperOptions;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import com.poimapper.util.ReflectionUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
public class ExcelRowBeanMapper {
    private ExcelRowBeanMapper(){

    }
    private ExcelRowBeanMapper(Builder builder){
        rowMapping = builder.options.getRowMappingOptions();
        customStringCastingFunc = builder.options.getCustomCastingFunc();
    }
    private  LinkedHashMap<String, Map<String,String>> rowMapping = null;
    private  CastString customStringCastingFunc = new DefaultCastString();

    private final DataFormatter dataFormatter = new DataFormatter();
    public  <T> T fromExcelRow(Row row, T dto)  {
        int i = 0;
        for (Map.Entry<String, Map<String,String>> entry : rowMapping.entrySet()) {
            String cellValue = dataFormatter.formatCellValue(row.getCell(i));
            String fieldMapName = entry.getValue().get("fieldMapping");
            try{
                setNestedValue(dto,fieldMapName,cellValue,entry.getValue());
            }catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("An Error Occurred while handling field " + fieldMapName, e);
            }
            i++;
        }
        return dto;
    }
    private  <T> void setNestedValue(T dto, String fieldMapName, String cellValue,Map<String,String> options) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Object currentClassInstance = dto;
        List<String> fields = new LinkedList<>(List.of(fieldMapName.split(":")));
        while (true){
            String curFieldMapName = fields.removeFirst();
            Field curField =  currentClassInstance.getClass().getDeclaredField(curFieldMapName);
            Method setterMethod = ReflectionUtil.getSetterMethod(currentClassInstance.getClass(), curFieldMapName,curField.getType());
            if(fields.isEmpty()) {
                setterMethod.invoke(currentClassInstance, customStringCastingFunc.apply(curField,cellValue,options) );
                break;
            }
            Method getterMethod = ReflectionUtil.getGetterMethod(currentClassInstance.getClass(), curFieldMapName);
            Object getterValue = getterMethod.invoke(currentClassInstance);
            Object nestedClassInstance = (getterValue == null) ?  curField.getType().getConstructor().newInstance() : getterValue;
            setterMethod.invoke(currentClassInstance,nestedClassInstance);
            currentClassInstance =  nestedClassInstance;
        }
    }
    public static class Builder {
        ExcelRowBeanMapperOptions options = ExcelRowBeanMapperOptions.getInstance();

        public Builder setRowMapping(LinkedHashMap<String, Map<String, String>> rowMapping) {
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
        public ExcelRowBeanMapper build() {
            if(options.getRowMappingOptions() == null)
                throw  new RuntimeException("Cannot build ExcelRowBeanMapper, rowMapping option is necessary to set");
            return new ExcelRowBeanMapper(this);
        }
    }
}

package com.poimapper;

import com.poimapper.config.CastString;
import com.poimapper.config.DefaultCastString;
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

    public  DataFormatter dataFormatter = new DataFormatter();

    public  <T> T fromExcelRow(Row row, T dto, LinkedHashMap<String, Map<String,String>> rowMapping)  {
        return fromExcelRow(row,dto,rowMapping,null);
    }
    public  <T> T fromExcelRow(Row row, T dto, LinkedHashMap<String, Map<String,String>> rowMapping, CastString convertValueToType)  {
        int i = 0;
        for (Map.Entry<String, Map<String,String>> entry : rowMapping.entrySet()) {
            String cellValue = dataFormatter.formatCellValue(row.getCell(i));
            String fieldMapName = entry.getValue().get("fieldMapping");
            try{
                setNestedValue(fieldMapName,dto,cellValue,convertValueToType);
            }catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                new RuntimeException("An Error Occurred while handling field "+fieldMapName,e);
            }
            i++;
        }
        return dto;
    }
    private  <T> void setNestedValue(String fieldMapName, T dto, String cellValue, CastString customCastStringFunc) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Object currentClassInstance = dto;
        List<String> fields = new LinkedList<>(List.of(fieldMapName.split(":")));
        while (true){
            String curFieldMapName = fields.removeFirst();
            Field curField =  currentClassInstance.getClass().getDeclaredField(curFieldMapName);
            Method setterMethod = ReflectionUtil.getSetterMethod(currentClassInstance.getClass(), curFieldMapName,curField.getType());
            if(fields.isEmpty()) {
                setterMethod.invoke(currentClassInstance, customCastStringFunc == null ? new DefaultCastString().apply(curField.getType(),cellValue) : customCastStringFunc.apply(curField.getType(),cellValue));
                break;
            }
            Method getterMethod = ReflectionUtil.getGetterMethod(currentClassInstance.getClass(), curFieldMapName);
            Object getterValue = getterMethod.invoke(currentClassInstance);
            Object nestedClassInstance = (getterValue == null) ?  curField.getType().getConstructor().newInstance() : getterValue;
            setterMethod.invoke(currentClassInstance,nestedClassInstance);
            currentClassInstance =  nestedClassInstance;
        }
    }
}

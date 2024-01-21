package com.poimapper.util;

import com.poimapper.ExcelRowBeanMapper;
import com.poimapper.config.DefaultCastString;
import com.poimapper.constants.ErrorCodes;
import com.poimapper.exception.MissingConfigurationException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ExcelSheetGeneratorUtil {
    private static final Logger logger = Logger.getLogger(DefaultCastString.class.getName());
    LinkedHashMap<String, Map<String, Object>> rowMapping;
    String path;
    String name;
    ExcelSheetGeneratorUtil(){}
    ExcelSheetGeneratorUtil(Builder builder){
        rowMapping = builder.rowMapping;
        path = builder.path;
        name = (builder.name == null ? "GeneratedExcel_"+new SecureRandom().nextInt(1000,100000)+".xlsx" : builder.name+".xlsx");
    }
    public boolean generate(String passedName){
        name = passedName != null ? passedName+".xlsx" : name;
        try(FileOutputStream fileOutputStream =new FileOutputStream(new File(Path.of(path,name).toUri())); Workbook workbook = new HSSFWorkbook();){
            Sheet worksheet = workbook.createSheet();
            CellStyle cellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            cellStyle.setFont(font);
            font.setBold(true);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            Row row = worksheet.getRow(0);
            if (row == null)
                row = worksheet.createRow(0);
            int col = 0;
            for(Map.Entry<String,Map<String,Object>> rowMap: rowMapping.entrySet()){
                Cell cell = row.createCell(col++);
                cell.setCellValue(rowMap.getKey());
                cell.setCellStyle(cellStyle);
            }
            workbook.write(fileOutputStream);
        }catch (Exception e){
            logger.warning("Failed to create sheet. "+e.getLocalizedMessage());
            return false;
        }
        return true;
    }
    public static class Builder {
        LinkedHashMap<String, Map<String, Object>> rowMapping;
        String path;
        String name;

        public Builder setRowMapping(LinkedHashMap<String, Map<String, Object>> rowMapping) {
            this.rowMapping =rowMapping;
            return this;
        }
        public Builder setPath(String path) {
            this.path =path;
            return this;
        }
        public Builder setFileName(String name) {
            this.name =name;
            return this;
        }
        public ExcelSheetGeneratorUtil build() {
            if(rowMapping == null)
                throw  new MissingConfigurationException(ErrorMessageGenerationUtil.getErrorMessage(ErrorCodes.MISSING_CONFIGURATION, ExcelSheetGeneratorUtil.class.getName(),"rowMapping"));
            if(path == null)
                throw  new MissingConfigurationException(ErrorMessageGenerationUtil.getErrorMessage(ErrorCodes.MISSING_CONFIGURATION,ExcelSheetGeneratorUtil.class.getName(),"path"));
            return new ExcelSheetGeneratorUtil(this);
        }
    }
}

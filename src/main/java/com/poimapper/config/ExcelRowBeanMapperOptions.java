package com.poimapper.config;

import com.poimapper.constants.CastConstants;
import lombok.Data;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ExcelRowBeanMapperOptions {
    @Getter
    private static final ExcelRowBeanMapperOptions instance = new ExcelRowBeanMapperOptions();

    private String datePattern = CastConstants.DATE_PATTERN;
    private String dateTimePattern = CastConstants.DATE_TIME_PATTERN;
    private PoiBuilderConfig poiBuilderConfig;
    private CastString customCastingFunc;
    private LinkedHashMap<String, PoiConfig> rowMappingOptions;
}

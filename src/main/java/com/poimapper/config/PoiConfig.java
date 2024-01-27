package com.poimapper.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PoiConfig {
    String fieldName;
    String pattern;
    Object defaultValue;

    public PoiConfig(){ }
    public PoiConfig(String fieldName){
        this.fieldName = fieldName;
    }
    public PoiConfig(String fieldName ,String pattern){
        this.pattern = pattern;
        this.fieldName = fieldName;
    }

    public PoiConfig(String fieldName,String pattern,Object defaultValue){
        this.fieldName = fieldName;
        this.pattern = pattern;
        this.defaultValue = defaultValue;
    }
}

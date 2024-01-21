package com.poimapper.exception;

public class MissingConfigurationException extends ExcelRowBeanMapperException{
    public MissingConfigurationException(String message){
        super(message);
    }
    public MissingConfigurationException(String message, Throwable throwable){
        super(message,throwable);
    }
}

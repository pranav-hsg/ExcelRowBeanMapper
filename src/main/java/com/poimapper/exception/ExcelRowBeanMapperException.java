package com.poimapper.exception;

public class ExcelRowBeanMapperException extends RuntimeException{
    public ExcelRowBeanMapperException(String message){
        super(message);
    }
    public ExcelRowBeanMapperException(String message, Throwable throwable){
        super(message,throwable);
    }
}

package com.poimapper.exception;

public class CastException extends ExcelRowBeanMapperException{
    public CastException(String message){
        super(message);
    }
    public CastException(String message, Throwable throwable){
        super(message,throwable);
    }
}

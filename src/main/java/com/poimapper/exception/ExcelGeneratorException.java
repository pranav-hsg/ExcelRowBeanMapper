package com.poimapper.exception;

public class ExcelGeneratorException extends ExcelRowBeanMapperException{
    public ExcelGeneratorException(String message) {
        super(message);
    }

    public ExcelGeneratorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

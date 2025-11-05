package com.pratham.bootbase.exception;

public class BadRequestException extends ApiException{
    public BadRequestException(String message){
        super(message,400);
    }
}

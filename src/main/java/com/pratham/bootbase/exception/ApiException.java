package com.pratham.bootbase.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private final int status;

    public ApiException(String message,int status){
        this.status=status;
        super(message);
    }
}

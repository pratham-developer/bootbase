package com.pratham.bootbase.advice;

import com.pratham.bootbase.dto.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

//to add timestamp automatically to all responses
//we can modify the response of all controllers using this class
//inherits the interface ResponseBodyAdvice
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    //return true for applying to the particular return types
    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true; //apply to all responses
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        if (body == null) return null;

        if (body instanceof ApiResponse<?>) {
            ((ApiResponse<?>) body).setTimestamp(OffsetDateTime.now(ZoneOffset.systemDefault()));
        }

        return body;
    }
}

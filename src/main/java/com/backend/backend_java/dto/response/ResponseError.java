package com.backend.backend_java.dto.response;

public class ResponseError<T> extends ResponseData<T> {

    public ResponseError(int status, String message) {
        super(status, message);
    }
}
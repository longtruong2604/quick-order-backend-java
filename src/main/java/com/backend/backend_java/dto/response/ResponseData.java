package com.backend.backend_java.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ResponseData<T> {
    private String message;
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // Constructor for PUT, PATCH, DELETE
    public ResponseData(int status, String message) {
        this.message = message;
        this.status = status;
        this.data = null; // Initialize data to a default value
    }

    // Constructor for POST, GET
    public ResponseData(int status, String message, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

}

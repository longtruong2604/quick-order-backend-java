package com.backend.backend_java.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import java.io.Serializable;

@Setter
@Getter
public class ErrorResponse implements Serializable {

    private int status;
    private String error;
    private String message;
    private String path;
    private Date timestamp;

}

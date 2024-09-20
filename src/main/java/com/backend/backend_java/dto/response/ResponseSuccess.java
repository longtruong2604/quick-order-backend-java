package com.backend.backend_java.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseSuccess<T> extends ResponseEntity<ResponseSuccess.Payload<T>> {

    public ResponseSuccess(HttpStatusCode status, String message) {
        super(new Payload<T>(message, status.value()), HttpStatus.OK);
    }

    public ResponseSuccess(HttpStatusCode status, String message, T data) {
        super(new Payload<T>(message, status.value(), data), HttpStatus.OK);
    }

    public static class Payload<T> {
        private final String message;
        private final Number status;
        private final T data;

        public Payload(String message, Number status, T data) {
            this.data = data;
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public Payload(String message, Number status) {
            this.message = message;
            this.status = status;
            this.data = null; // Initialize data to a default value
        }

        public Number getStatus() {
            return status;
        }

        public T getData() {
            return data;
        }
    }

}

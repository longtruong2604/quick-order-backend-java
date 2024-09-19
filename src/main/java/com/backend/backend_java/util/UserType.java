package com.backend.backend_java.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("USER")
    USER,
    @JsonProperty("owner")
    OWNER;
}

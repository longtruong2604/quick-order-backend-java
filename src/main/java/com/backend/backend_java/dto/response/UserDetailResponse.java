package com.backend.backend_java.dto.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailResponse implements Serializable {
    private Long id;
    private String firstName;
    private String userName;
    private String lastName;
    private String phone;
    private String email;

    public UserDetailResponse(Long id, String firstName, String lastName, String phone, String email, String userName) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }
}

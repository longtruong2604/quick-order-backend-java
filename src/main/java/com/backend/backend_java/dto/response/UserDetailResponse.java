package com.backend.backend_java.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String userName;
}

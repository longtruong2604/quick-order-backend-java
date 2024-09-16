package com.backend.backend_java.service;

import com.backend.backend_java.dto.request.UserRequestDTO;

public interface UserService {
    int addUser(UserRequestDTO requestDTO);
}

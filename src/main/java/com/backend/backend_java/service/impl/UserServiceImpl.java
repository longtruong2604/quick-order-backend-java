package com.backend.backend_java.service.impl;

import org.springframework.stereotype.Service;

import com.backend.backend_java.dto.request.UserRequestDTO;
import com.backend.backend_java.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public int addUser(UserRequestDTO requestDTO) {
        System.out.println("Add user to database");
        if (requestDTO.getFirstName() == null) {
            throw new RuntimeException("First name is required");
        }
        return 0;

    }

}
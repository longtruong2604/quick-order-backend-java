package com.backend.backend_java.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_java.dto.request.UserRequestDTO;
import com.backend.backend_java.dto.request.UserRequestDTO.Address;
import com.backend.backend_java.dto.response.ResponseData;
import com.backend.backend_java.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.Date;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequestDTO user) {
        System.out.println("Request add user " + user.getFirstName());
        try {
            userService.addUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) int userId, @Valid @RequestBody UserRequestDTO user) {
        System.out.println("Request update userId=" + userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully");
    }

    @PatchMapping("/{userId}")
    public ResponseData<?> updateStatus(@Min(1) @PathVariable int userId, @RequestParam boolean status) {
        System.out.println("Request change status, userId=" + userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User's status changed successfully");
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(
            @PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId) {
        System.out.println("Request delete userId=" + userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully");
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable @Min(1) int userId) {
        System.out.println("Request get user detail, userId=" + userId);
        return new ResponseData<>(HttpStatus.OK.value(), "user",
                new UserRequestDTO("Tay", "Java", "admin@tayjava.vn", "0123456789", new Date(), "admin", "admin",
                        Set.of(new Address("123", "Hanoi", "Vietnam", "admin", "admin", "admin", "admin", 1))));
    }

    @GetMapping("/list")
    public ResponseData<List<UserRequestDTO>> getAllUser(@RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        System.out.println("Request get all of users");
        return new ResponseData<>(HttpStatus.OK.value(), "user",
                List.of(new UserRequestDTO("Tay", "Java", "admin@tayjava.vn", "0123456789", new Date(), "admin",
                        "admin",
                        Set.of(new Address("123", "Hanoi", "Vietnam", "admin", "admin", "admin", "admin", 1)))));
    }
}
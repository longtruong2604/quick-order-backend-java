package com.backend.backend_java.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_java.dto.request.UserRequestDTO;
import com.backend.backend_java.dto.response.ResponseData;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/")
    public ResponseData<UserRequestDTO> addUser(@Valid @RequestBody UserRequestDTO user) {
        return new ResponseData<>(HttpStatus.CREATED.value(), "User created", user);
    }

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) int userId, @Valid @RequestBody UserRequestDTO user) {
        System.out.println("Update user");
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated");
    }

    @PatchMapping("/{userId}")
    public ResponseData<?> updateStatus(@Min(1) @PathVariable int userId,
            @RequestParam boolean status) {
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User status updated");
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(
            @PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId) {
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted");
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable @Min(1) int userId) {
        return new ResponseData<>(HttpStatus.OK.value(), "User found",
                new UserRequestDTO("Tay", "Java", "admin@tayjava.vn", "0123456789"));
    }

    @GetMapping("/list")
    public ResponseData<List<UserRequestDTO>> getAllUser(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        return new ResponseData<>(HttpStatus.OK.value(), "List of users",
                List.of(new UserRequestDTO("Tay", "Java", "admin@tayjava.vn", "0123456789"),
                        new UserRequestDTO("Leo", "Messi", "leomessi@email.com", "0123456456")));
    }
}

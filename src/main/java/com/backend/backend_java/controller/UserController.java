package com.backend.backend_java.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_java.dto.request.UserRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @PostMapping("/")
    public String addUser(@Valid @RequestBody UserRequestDTO user) {
        return "User added";
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable @Min(1) int userId, @Valid @RequestBody UserRequestDTO user) {
        System.out.println("Update user");
        return "User updated";
    }

    @PatchMapping("/{userId}")
    public String updateStatus(@Min(1) @PathVariable int userId,
                               @RequestParam boolean status) {
        return "User Status changed";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId) {
        return "User deleted";
    }

    @GetMapping("/{userId}")
    public UserRequestDTO getUser(@PathVariable @Min(1) int userId) {
        return new UserRequestDTO("Tay", "Java", "admin@tayjava.vn", "0123456789");
    }

    @GetMapping("/list")
    public List<UserRequestDTO> getAllUser(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                           @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        return List.of(new UserRequestDTO("Tay", "Java", "admin@tayjava.vn", "0123456789"),
                new UserRequestDTO("Leo", "Messi", "leomessi@email.com", "0123456456"));
    }
}

package com.auction.server.controllers;

import com.auction.server.dtos.UserDto;
import com.auction.server.entities.User;
import com.auction.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserDto dto) {
        User user = userService.createUser(dto);
        return ResponseEntity.ok(String.valueOf(user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto dto) {
        Optional<User> userOpt = userService.getUserByEmailAndPassword(dto.getEmail(), dto.getPassword());
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(String.valueOf(userOpt.get().getId()));
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

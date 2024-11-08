package com.example.Tokk.Controllers;

import com.example.Tokk.Model.Users;
import com.example.Tokk.Service.ServiceRegistr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ControllerLoad {

    private final ServiceRegistr serviceRegistr;

    @Autowired
    public ControllerLoad(ServiceRegistr serviceRegistr) {
        this.serviceRegistr = serviceRegistr;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users user) {
        log.info("Registering user {}", user);
        return serviceRegistr.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        return serviceRegistr.loginUser(email, password);
    }

    @GetMapping("/user")
    public String userGet(){
        return "hello user";
    }
    @GetMapping("/admin")
    public String adminGet(){
        return "hello admin";
    }

    @GetMapping("/")
    public ResponseEntity<String> allGet(){
        // Извлекаем информацию о текущем пользователе из SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Если пользователь авторизован, выводим его имя
            String username = authentication.getName();  // Имя пользователя (например, email)
            return ResponseEntity.ok("Hello, " + username);
        }

        // Если пользователь не авторизован, выводим "Гость"
        return ResponseEntity.ok("Hello, Гость");
    }

}

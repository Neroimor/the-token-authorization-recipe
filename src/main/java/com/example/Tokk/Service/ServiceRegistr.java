package com.example.Tokk.Service;

import com.example.Tokk.Components.JwtTokenProvider;
import com.example.Tokk.Model.Users;
import com.example.Tokk.Repository.UsersJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceRegistr {
    private static final Logger log = LoggerFactory.getLogger(ServiceRegistr.class);
    private final UsersJpaRepository usersJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ServiceRegistr(UsersJpaRepository usersJpaRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.usersJpaRepository = usersJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }



    public ResponseEntity<String> registerUser(Users user) {
        log.info(user.toString());
        if (usersJpaRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER"); // Назначаем роль по умолчанию
            usersJpaRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.badRequest().body("User already exists");
        }
    }

    public ResponseEntity<String> loginUser(String email, String password) {
        Optional<Users> user = usersJpaRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            String token = jwtTokenProvider.createToken(email, user.get().getRole());
            log.info(token);
            return ResponseEntity.ok("Bearer " + token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

}

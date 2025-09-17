package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.dto.request.LoginRequest;
import com.example.GreenCareAI.dto.request.RegisterRequest;
import com.example.GreenCareAI.entity.User;
import com.example.GreenCareAI.enums.Role;
import com.example.GreenCareAI.repository.UserRepository;
import com.example.GreenCareAI.security.JwtService;
import com.example.GreenCareAI.service.LoginHistoryService;   // ✅ Thêm dòng này

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;   // ✅ Thêm dòng này

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final LoginHistoryService loginHistoryService;  // ✅ Inject service

    // ✅ Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username đã tồn tại");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok().body(
                Map.of(
                        "message", "Register thành công",
                        "token", token,
                        "username", user.getUsername(),
                        "role", user.getRole().name()
                )
        );
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        String token = jwtService.generateToken(user);

        // ✅ Ghi log login
        loginHistoryService.saveLogin(user);

        return ResponseEntity.ok().body(
                Map.of(
                        "message", "Login success",
                        "token", token,
                        "username", user.getUsername()
                )
        );
    }
}

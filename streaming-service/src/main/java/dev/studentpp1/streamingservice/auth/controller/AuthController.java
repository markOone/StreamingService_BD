package dev.studentpp1.streamingservice.auth.controller;

import dev.studentpp1.streamingservice.auth.service.AuthService;
import dev.studentpp1.streamingservice.users.dto.LoginUserRequest;
import dev.studentpp1.streamingservice.users.dto.RegisterUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserRequest request,
                                         HttpServletRequest httpServletRequest) throws Exception {
        authService.register(request, httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginUserRequest request,
                                      HttpServletRequest httpServletRequest) {
        authService.login(request, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}

package com.owt.challenge.boat.controller;


import com.owt.challenge.boat.controller.response.JwtResponse;
import com.owt.challenge.boat.controller.request.LoginRequest;
import com.owt.challenge.boat.controller.request.RegisterRequest;
import com.owt.challenge.boat.controller.response.RegisterResponse;
import com.owt.challenge.boat.domain.User;
import com.owt.challenge.boat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest){
            return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser (@RequestBody RegisterRequest registerRequest) {
        User user = userService.register(registerRequest);
        return ResponseEntity.ok(new RegisterResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

}

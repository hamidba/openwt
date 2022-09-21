package com.owt.challenge.boat.service;

import com.owt.challenge.boat.controller.request.LoginRequest;
import com.owt.challenge.boat.controller.request.RegisterRequest;
import com.owt.challenge.boat.controller.response.JwtResponse;
import com.owt.challenge.boat.domain.User;
import com.owt.challenge.boat.repository.UserRepository;
import com.owt.challenge.boat.security.jwt.TokenProvider;
import com.owt.challenge.boat.service.exception.EmailAlreadyUsedException;
import com.owt.challenge.boat.service.exception.UsernameAlreadyUsedException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    public JwtResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate
                        (new UsernamePasswordAuthenticationToken
                                (loginRequest.username(),
                                        loginRequest.password()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        String jwt = tokenProvider.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails)
                authentication.getPrincipal();

        return new JwtResponse(jwt, "Bearer",  userDetails.getUsername());
    }

    public User register(RegisterRequest registerRequest){

        if (userRepository.existsByUsername(registerRequest.username())) throw new UsernameAlreadyUsedException();
        if (userRepository.existsByEmail (registerRequest.email())) throw new EmailAlreadyUsedException();

        // Create new user account
        User user = User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .password(encoder.encode(registerRequest.password())).build();

        userRepository.save(user);
        return user;

    }

}

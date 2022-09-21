package com.owt.challenge.boat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owt.challenge.boat.BoatApplication;
import com.owt.challenge.boat.controller.request.LoginRequest;
import com.owt.challenge.boat.controller.request.RegisterRequest;
import com.owt.challenge.boat.repository.UserRepository;
import com.owt.challenge.boat.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BoatApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;




    @Test
    void givenUnRegisteredUser_whenCallControllerLogin_thenReturnUnauthorized() throws Exception {

        mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("user_not_exist", "user"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenRegisteredUser_whenCallControllerLogin_thenReturnAuthorized() throws Exception {

        userService.register(new RegisterRequest("johndoe", "john@doe.com", "password"));

        mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest("johndoe", "john@doe.com", "password"))))
                .andExpect(status().isOk());


    }

    @Test
    void givenUnRegisteredUser_whenCallControllerRegister_thenReturnNewUser() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("misterBean", "mister@bean.com", "password");
        long databaseSizeBeforeCreate = userRepository.count();

        mockMvc
                .perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value(registerRequest.username()))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.email").value(registerRequest.email()));


        assertThat(userRepository.count()).isEqualTo(databaseSizeBeforeCreate + 1);



    }
}

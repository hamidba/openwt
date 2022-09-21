package com.owt.challenge.boat.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.owt.challenge.boat.BoatApplication;
import com.owt.challenge.boat.controller.dto.BoatDTO;
import com.owt.challenge.boat.domain.Boat;
import com.owt.challenge.boat.repository.BoatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest(classes = BoatApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class BoatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenUnauthenticated_whenCallController_thenReturnUnauthorized() throws Exception {
        mockMvc
            .perform(get("/api/v1/boats").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    void givenAuthenticated_whenCallController_thenReturnResult() throws Exception {
        mockMvc
                .perform(get("/api/v1/boats").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    void givenBoatWithID_whenCallControllerCreate_thenReturnBadRequest() throws Exception {

        BoatDTO boatDTO = BoatDTO.builder().id(1L).name("Boat").description("Boat Description").build();

        mockMvc
                .perform(post("/api/v1/boats")
                        .content(objectMapper.writeValueAsString(boatDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "user")
    void givenBoat_whenCallControllerCreate_thenReturnCreated() throws Exception {

        int databaseSizeBeforeCreate = boatRepository.findAll().size();

        BoatDTO boatDTO = BoatDTO.builder().name("Boat").description("Boat Description").build();

        mockMvc
              .perform(post("/api/v1/boats")
                      .content(objectMapper.writeValueAsString(boatDTO))
                      .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(boatDTO.getName()))
                .andExpect(jsonPath("$.description").value(boatDTO.getDescription()));


        assertThat(boatRepository.count()).isEqualTo(databaseSizeBeforeCreate + 1);

    }

    @Test
    @WithMockUser(username = "user")
    void givenBoatIdNotExisting_whenCallControllerGet_thenReturnNotFound() throws Exception {

        mockMvc
                .perform(get("/api/v1/boats/{id}", 11)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void givenBoatExistingId_whenCallControllerGet_thenReturnBoat() throws Exception {

        Boat boat = boatRepository.save(Boat.builder().name("Boat").description("Description").build());

        mockMvc
                .perform(get("/api/v1/boats/{id}", boat.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(boat.getId()))
                .andExpect(jsonPath("$.name").value(boat.getName()))
                .andExpect(jsonPath("$.description").value(boat.getDescription()));
    }

    @Test
    @WithMockUser(username = "user")
    void givenNotExistingBoatID_whenCallControllerUpdate_thenReturnNotFound() throws Exception {

        BoatDTO boatUpdatedDTO = BoatDTO.builder().id(11L).name("Boat Updated").description("Description Updated").build();

        mockMvc
                .perform(put("/api/v1/boats/{id}", boatUpdatedDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boatUpdatedDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void givenEmptyBoatID_whenCallControllerUpdate_thenReturnBadRequest() throws Exception {
        BoatDTO boatUpdatedDTO = BoatDTO.builder().name("Boat Updated").description("Description Updated").build();
        mockMvc
                .perform(put("/api/v1/boats/{id}", 11L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boatUpdatedDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void givenExistingBoat_whenCallControllerUpdate_thenReturnUpdatedBoat() throws Exception {

        Boat boat = boatRepository.save(Boat.builder().name("Boat").description("Description").build());
        BoatDTO boatUpdatedDTO = BoatDTO.builder().id(boat.getId()).name("Boat Updated").description("Description Updated").build();

        mockMvc
                .perform(put("/api/v1/boats/{id}", boat.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boatUpdatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(boatUpdatedDTO.getId()))
                .andExpect(jsonPath("$.name").value(boatUpdatedDTO.getName()))
                .andExpect(jsonPath("$.description").value(boatUpdatedDTO.getDescription()));

        Optional<Boat> optionalBoat = boatRepository.findById(boat.getId());
        assertThat(optionalBoat).isPresent();
        Boat boatUpdatedFromDB = optionalBoat.get();
        assertThat(boatUpdatedFromDB.getName()).isEqualTo(boatUpdatedDTO.getName());
        assertThat(boatUpdatedFromDB.getDescription()).isEqualTo(boatUpdatedDTO.getDescription());
    }

    @Test
    @WithMockUser(username = "user")
    void givenExistingBoat_whenCallControllerDelete_thenReturnNoContent() throws Exception {

        Boat boat = boatRepository.save(Boat.builder().name("Boat").description("Description").build());

        mockMvc
                .perform(delete("/api/v1/boats/{id}", boat.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Boat> optionalBoat = boatRepository.findById(boat.getId());
        assertThat(optionalBoat).isNotPresent();
    }



}

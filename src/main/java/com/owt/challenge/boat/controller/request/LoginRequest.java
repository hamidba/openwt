package com.owt.challenge.boat.controller.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record LoginRequest ( @NotNull @Size(min = 1, max = 50) String username,
                             @NotNull @Size(min = 4, max = 100) String password) {}

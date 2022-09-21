package com.owt.challenge.boat.controller.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RegisterRequest(@NotNull @Size(min = 1, max = 50) String username,
                              @NotNull String email,
                              @NotNull @Size(min = 4, max = 100) String password) {}

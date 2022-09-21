package com.owt.challenge.boat.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class BoatDTO {

    private Long id;
    private String name;
    private String description;

}

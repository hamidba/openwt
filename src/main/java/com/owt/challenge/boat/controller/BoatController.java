package com.owt.challenge.boat.controller;

import com.owt.challenge.boat.controller.dto.BoatDTO;
import com.owt.challenge.boat.domain.Boat;
import com.owt.challenge.boat.repository.BoatRepository;
import com.owt.challenge.boat.service.BoatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/boats")
@AllArgsConstructor
@Slf4j
public class BoatController {

    private final BoatRepository boatRepository;

    @GetMapping
    public List<BoatDTO> getBoats(){
        log.info("REST request to get all Boats");
        return boatRepository.findAll().stream().map(b -> BoatDTO.builder()
                .id(b.getId())
                .name(b.getName())
                .description(b.getDescription()).build()).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoatDTO> getBoat(@PathVariable Long id){
        log.info("REST request to get Boat with ID : {}", id);
        return boatRepository.findById(id)
                .map(b -> ResponseEntity.ok().body(BoatDTO.builder()
                        .id(b.getId())
                        .name(b.getName())
                        .description(b.getDescription()).build()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BoatDTO> createBoat(@RequestBody BoatDTO boatDTO) throws URISyntaxException {
        log.info("REST request to create Boat : {}", boatDTO);
        if(boatDTO.getId() != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new Boat cannot contain an ID");
        Boat boat = Boat.builder().name(boatDTO.getName()).description(boatDTO.getDescription()).build();
        Boat result = boatRepository.save(boat);
        return ResponseEntity
                .created(new URI("/api/v1/boats/" + result.getId()))
                .body(BoatDTO.builder()
                        .id(result.getId())
                        .name(result.getName())
                        .description(result.getDescription()).build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<BoatDTO> updateBoat(@PathVariable Long id, @RequestBody BoatDTO boatDTO){
        log.info("REST request to update Boat : {}", boatDTO);
        if (boatDTO.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        if (!Objects.equals(id, boatDTO.getId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        if (!boatRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Boat not found");

        return boatRepository.findById(id)
                .map(b -> {
                    b.setName(boatDTO.getName());
                    b.setDescription(boatDTO.getDescription());
                    return b;
                })
                .map(boatRepository::save)
                .map(b -> ResponseEntity.ok().body(BoatDTO.builder()
                        .id(b.getId())
                        .name(b.getName())
                        .description(b.getDescription()).build()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoat(@PathVariable Long id){
        log.info("REST request to delete Boat : {}", id);
        boatRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

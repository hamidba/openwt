package com.owt.challenge.boat.service;

import com.owt.challenge.boat.domain.Boat;
import com.owt.challenge.boat.repository.BoatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoatService {

    private final BoatRepository boatRepository;

    public List<Boat> getBoats(){
        return boatRepository.findAll();
    }

}

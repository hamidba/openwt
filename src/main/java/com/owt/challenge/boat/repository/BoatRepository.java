package com.owt.challenge.boat.repository;

import com.owt.challenge.boat.domain.Boat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatRepository extends JpaRepository<Boat, Long> {}

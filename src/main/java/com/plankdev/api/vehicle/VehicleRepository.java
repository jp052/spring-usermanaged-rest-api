package com.plankdev.api.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByName(String name);
    
    Optional<Vehicle> findByIdAndApplicationName(Long id, String applicationName);
}

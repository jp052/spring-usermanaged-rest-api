package com.plankdev.api.vehicle;

import com.plankdev.security.dataaccess.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {
    private VehicleRepository vehicleRepo;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public Optional<Vehicle> createVehicle(Vehicle vehicle, Application app) {
    	vehicle.setApplication(app);
        Optional<Vehicle> vehicleOptional = Optional.of(vehicleRepo.save(vehicle));
        return vehicleOptional;
    }

	public Optional<Vehicle>  findByIdAndApplicationId(Long vehicleId, Application app) {
		Optional<Vehicle> vehicle = vehicleRepo.findByIdAndApplicationName(vehicleId, app.getName());
		return vehicle;
	}
}

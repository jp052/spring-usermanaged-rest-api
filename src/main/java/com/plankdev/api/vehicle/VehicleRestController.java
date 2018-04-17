package com.plankdev.api.vehicle;

import com.plankdev.restcommons.ResponseBuilder;
import com.plankdev.security.dataaccess.Application;
import com.plankdev.security.dataaccess.ApplicationRepository;
import com.plankdev.security.dataaccess.UserService;
import com.plankdev.security.exception.AppNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@RequestMapping(value = "/api/v1/vehicles")
@RestController
public class VehicleRestController {

    private VehicleService vehicleService;
    private UserService userService;
    private ApplicationRepository appRepo;

    @Autowired
    public VehicleRestController(VehicleService vehicleService, UserService userService, ApplicationRepository appRepo) {
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.appRepo = appRepo;
    }

    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody Vehicle vehicle) {
    	Application app = getCurrentApplication();
        Optional<Vehicle> createdVehicle = vehicleService.createVehicle(vehicle, app);
        ResponseEntity<?> response = ResponseBuilder.buildNewModelResponseEntity(createdVehicle);

        return response;
    }
    
    @GetMapping("/{vehicleId}")
    public Vehicle readVehilce(@PathVariable Long vehicleId) {
    	Application app = getCurrentApplication();
    	Optional<Vehicle> vehicle = vehicleService.findByIdAndApplicationId(vehicleId, app);
    	return vehicle.orElseThrow(() -> new EntityNotFoundException("vehicle could not be found with id: " + vehicleId));    	
    }
    
    //FIXME: move to SpringSecurityAppContext
	private Application getCurrentApplication() {	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Object details = authentication.getDetails();
		HashMap<String, Object> tokenDetails = (HashMap<String, Object>) details;
		Object object = tokenDetails.get(Application.APP_NAME_KEY);
		String appName = (String)object;
		
		Optional<Application> app = Optional.empty();
		if(appName != null) {
			try {
				app = appRepo.findByName(appName);
			} catch (Exception e) {
				e.printStackTrace();			
			}			
		}
	
		return app.orElseThrow(() -> new AppNotFoundException());
	}

}

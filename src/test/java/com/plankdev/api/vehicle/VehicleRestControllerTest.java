package com.plankdev.api.vehicle;

import com.plankdev.security.dataaccess.Application;
import com.plankdev.testutils.BaseRestControllerTest;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


public class VehicleRestControllerTest extends BaseRestControllerTest {

    @Test
    public void dummyTest() {
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "customUserDetailsService")
    public void createVehicleWorksWhenAllValuesSet() throws Exception {
        //assemble
        Application app = createApplication();

        String VEHICLE_NAME_EXPECTED = "car1";
        Vehicle vehicle = new Vehicle();
        vehicle.setName(VEHICLE_NAME_EXPECTED);

        String vehicleJson = jsonUtils.pojoToJson(vehicle);

        //action
        ResultActions performRequest = mockMvc.perform(post("/api/v1/vehicles")
        		.header("Authorization", "Bearer " + app.getApiKey().getJwtToken())
                .contentType(contentType)          
                .content(vehicleJson))
                .andDo(print());

        //assert
        performRequest
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(VEHICLE_NAME_EXPECTED));
    }

    private Application createApplication() throws Exception {
        final String APP_NAME_EXPECTED = "myApp1";
        Application newApp = new Application();
        newApp.setName(APP_NAME_EXPECTED);
        String applicationJson = jsonUtils.pojoToJson(newApp);

        MvcResult createApplicationResult = mockMvc.perform(post("/api/applications")
                .contentType(contentType)
                .content(applicationJson))
                .andReturn();

        String applicationJsonAsString = createApplicationResult.getResponse().getContentAsString();
        Application createdApp = jsonUtils.jsonStringToPojo(applicationJsonAsString, Application.class);
        return createdApp;
    }


    //createVehicleFailsWhenApplicationDoesNotMatchApiKey
}

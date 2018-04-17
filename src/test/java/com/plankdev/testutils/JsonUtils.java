package com.plankdev.testutils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;

/**
 * Utility class to create json from a POJO or the other way around.
 */
public class JsonUtils {

    private static ObjectMapper mapperIgnoringFails = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    public JsonUtils(HttpMessageConverter mappingJackson2HttpMessageConverter) {
        if (mappingJackson2HttpMessageConverter == null) {
            throw new NullPointerException("mappingJackson2HttpMessageConverter has to be set");
        }
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    //TODO: remove static when AppUserRestContrllerTest extends BaseRestControllerTest
    public static <T> T jsonStringToPojo(String jsonString, Class<T> clazz) throws IOException {
        T model = mapperIgnoringFails.readValue(jsonString, clazz);
        return model;
    }

    public String pojoToJson(Object obj) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(obj, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}

package com.plankdev.security.controller;

import com.plankdev.security.dataaccess.AppUser;
import com.plankdev.testutils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: extend with BaseRestControllerTest
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration //FIXME: is @WebAppConfiguration needed?
public class AppUserRestControllerTest {

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext context;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        //TODO: check if best assert
        assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithAnonymousUser
    public void shouldGetUnauthorizedWithoutRole() throws Exception {

        mockMvc.perform(get("api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    public void createUserSuccessWithAnonymousUser() throws Exception {
        //assemble
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        appUser.setPassword("password");

        String userJson = json(appUser);
        userJson = passwordIgnoreWorkaround(userJson);

        //action
        ResultActions perform = mockMvc.perform(post("/api/users")
                .contentType(contentType)
                .content(userJson));

        //assert
        perform.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void readUserSuccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateUserSuccessWithAdminRole() throws Exception {
        MvcResult getUserResult = mockMvc.perform(get("/api/users/1"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String userJsonAsString = getUserResult.getResponse().getContentAsString();

        AppUser appUser = JsonUtils.jsonStringToPojo(userJsonAsString, AppUser.class);
        appUser.setFirstName("firstName");

        ResultActions resultActions = mockMvc.perform(put("/api/users/1")
                .contentType(contentType)
                .content(json(appUser)));

        resultActions.andDo(print()).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteUserSuccessWithAdminRole() throws Exception {
        AppUser appUser = createNewUser();

        ResultActions resultActions = mockMvc.perform(delete("/api/users/" + appUser.getId()))
                .andDo(print());

        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void listUserSuccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void listUserFailWithUserRole() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getPersonsSuccessfullyWithUserRole() throws Exception {
        ResultActions perform = this.mockMvc.perform(get("/api/users/whoami"));
        perform.andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    public void getPersonsFailWithAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/users/whoami"))
                .andExpect(status().is4xxClientError());
    }

    private AppUser createNewUser() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser2");
        appUser.setPassword("password");

        String userJson = json(appUser);
        userJson = passwordIgnoreWorkaround(userJson);

        MvcResult createUserResutl = mockMvc.perform(post("/api/users")
                .contentType(contentType)
                .content(userJson))
                .andReturn();

        String userJsonAsString = createUserResutl.getResponse().getContentAsString();
        return JsonUtils.jsonStringToPojo(userJsonAsString, AppUser.class);
    }

    /**
     * The password should not be deserialized in the real application, but in tests deserialization is needed.
     *
     * @param userJson
     * @return
     */
    private String passwordIgnoreWorkaround(String userJson) {
        StringBuilder builder = new StringBuilder(userJson);
        builder.deleteCharAt(builder.length() - 1);
        builder.append(",");
        builder.append("\"password\":\"password\"");
        builder.append("}");
        userJson = builder.toString();
        return userJson;
    }

    protected String json(Object obj) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(obj, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}

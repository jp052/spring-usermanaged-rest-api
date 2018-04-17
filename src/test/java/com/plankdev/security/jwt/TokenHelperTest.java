package com.plankdev.security.jwt;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;



public class TokenHelperTest {
	
	private TokenHelper tokenHelper = new TokenHelper();
	
	@Before
	public void initTokenHelper() {
		tokenHelper.setAppName("spring-usermanaged-rest-api");
		tokenHelper.setAuthHeader("Authorization");
		tokenHelper.setExpiresIn(946080000); //30 years in seconds
		tokenHelper.setSecret("mysecret");
	}
	
	@Test
	public void generateTokenWithAppNameWorks() {
		//Assemble
		String USER_NAME_EXPECTED = "testUser";
		String APP_NAME_EXPECTED = "myApp1";
		
		//action
		String token = tokenHelper.generateToken(USER_NAME_EXPECTED, APP_NAME_EXPECTED);
		String userName = tokenHelper.getUsernameFromToken(token);
		String appName = tokenHelper.getAppnameFromToken(token);
		
		//assert
		assertThat(token, is(not(nullValue())));
		assertThat(userName, is(USER_NAME_EXPECTED));	
		assertThat(appName, is(APP_NAME_EXPECTED));
	}
	
	@Test
	public void generateTokenWithOnlyUsernameWorks() {
		//Assemble
		String USER_NAME_EXPECTED = "testUser";
		
		//action
		String token = tokenHelper.generateToken(USER_NAME_EXPECTED);
		String userName = tokenHelper.getUsernameFromToken(token);
		
		//assert
		assertThat(token, is(not(nullValue())));
		assertThat(userName, is(USER_NAME_EXPECTED));	
	}

}

package com.roal.survey_engine.security;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationFacadeTest {

    @Autowired
    private AuthenticationFacade authenticationFacade;


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testIsAdmin() {
        assertTrue(authenticationFacade.isAdmin());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void testIsNotAdmin() {
        assertFalse(authenticationFacade.isAdmin());
    }

    @Test
    @WithMockUser(username = "user1")
    public void testGetUsername() {
        assertEquals("user1", authenticationFacade.getUserDetails().getUsername());
    }

}

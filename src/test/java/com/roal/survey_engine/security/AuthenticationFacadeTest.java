package com.roal.survey_engine.security;


import com.roal.survey_engine.domain.user.UserAuthority;
import com.roal.survey_engine.domain.user.dto.UserRegistrationDto;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.repository.RoleRepository;
import com.roal.survey_engine.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationFacadeTest {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    public void setup() {

        roleRepository.save(new Role(UserAuthority.Constants.USER_VALUE));
        roleRepository.save(new Role(UserAuthority.Constants.ADMIN_VALUE));

        userService.create(new UserRegistrationDto("admin",
                "password",
                "password",
                "admin@example.com",
                Set.of(UserAuthority.Constants.ADMIN_VALUE)));

        userService.create(new UserRegistrationDto("user1",
                "password",
                "password",
                "user1@example.com",
                Set.of(UserAuthority.Constants.USER_VALUE)));
    }

    @Test
    @WithMockCustomUser(username = "admin")
    public void testIsAdmin() {
        assertTrue(authenticationFacade.isAdmin());
    }

    @Test
    @WithMockCustomUser(username = "user1")
    public void testIsNotAdmin() {
        assertFalse(authenticationFacade.isAdmin());
    }

    @Test
    @WithMockCustomUser(username = "user1")
    public void testGetUsername() {
        assertEquals("user1", authenticationFacade.getUserDetails().getUsername());
    }

    @Test
    @WithMockCustomUser(username = "user2")
    public void testUserNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> authenticationFacade.getUserDetails().getUsername());
    }

}

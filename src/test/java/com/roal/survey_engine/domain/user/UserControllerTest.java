package com.roal.survey_engine.domain.user;

import com.roal.survey_engine.domain.user.controller.UserController;
import com.roal.survey_engine.domain.user.dto.UserDto;
import com.roal.survey_engine.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    public void forbiddenAsAnonymous() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/users/me"))
            .andReturn()
            .getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "user1")
    public void successAsUser() throws Exception {

        given(userService.findByUsername("user1"))
            .willReturn(new UserDto("aaa", "user1", Set.of("ROLE_USER")));

        MockHttpServletResponse response = mvc.perform(get("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

        verify(userService, times(1)).findByUsername("user1");

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"id\":\"aaa\",\"username\":\"user1\",\"roles\":[\"ROLE_USER\"]}", response.getContentAsString());
    }
}

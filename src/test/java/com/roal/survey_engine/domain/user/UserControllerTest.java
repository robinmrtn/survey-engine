package com.roal.survey_engine.domain.user;

import com.roal.survey_engine.domain.user.controller.UserController;
import com.roal.survey_engine.domain.user.dto.UserDto;
import com.roal.survey_engine.domain.user.service.UserService;
import com.roal.survey_engine.security.jwt.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithAnonymousUser
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
            .willReturn(new UserDto("aaa", "user1", "user@example.de", Set.of("ROLE_USER"), false));

        mvc.perform(get("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"aaa\",\"username\":\"user1\",\"roles\":[\"ROLE_USER\"],\"isAdmin\":false}"));

        verify(userService, times(1)).findByUsername("user1");
    }
}

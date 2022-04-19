package com.roal.survey_engine.domain.survey;

import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.service.WorkspaceService;
import com.roal.survey_engine.domain.user.entity.User;
import com.roal.survey_engine.security.AuthenticationFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkspaceServiceTest {

    @Autowired
    WorkspaceService workspaceService;

    @MockBean
    AuthenticationFacade authenticationFacade;

    @Test
    public void testUserIsPartOfWorkspace() {
        Workspace workspace = new Workspace().addUser(new User("user1"));
        var loggedInUser = new org.springframework.security.core.userdetails.User("user1", "pw",
            Set.of(new SimpleGrantedAuthority("USER")));

        given(authenticationFacade.getUserDetails()).willReturn(loggedInUser);

        boolean actual = workspaceService.currentUserCanModifyWorkspace(workspace);

        verify(authenticationFacade, times(1)).getUserDetails();
        assertTrue(actual);
    }

    @Test
    public void testUserIsNotPartOfWorkspace() {
        Workspace workspace = new Workspace().addUser(new User("user1"));
        var loggedInUser = new org.springframework.security.core.userdetails.User("user2", "pw",
            Set.of(new SimpleGrantedAuthority("USER")));

        given(authenticationFacade.getUserDetails()).willReturn(loggedInUser);

        boolean actual = workspaceService.currentUserCanModifyWorkspace(workspace);

        verify(authenticationFacade, times(1)).getUserDetails();
        assertFalse(actual);
    }

    @Test
    public void testAdminCanEditWorkspace() {
        Workspace workspace = new Workspace().addUser(new User("user1"));
        var loggedInUser = new org.springframework.security.core.userdetails.User("user2", "pw",
            Set.of(new SimpleGrantedAuthority("ADMIN")));

        var loggedInAuth = new UsernamePasswordAuthenticationToken(loggedInUser, null);

        given(authenticationFacade.getUserDetails()).willReturn(loggedInUser);
        given(authenticationFacade.getAuthentication()).willReturn(loggedInAuth);

        boolean actual = workspaceService.currentUserCanModifyWorkspace(workspace);

        verify(authenticationFacade, times(1)).getUserDetails();
        assertFalse(actual);
    }

}

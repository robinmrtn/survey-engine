package com.roal.survey_engine.domain.survey;

import com.roal.survey_engine.domain.survey.dto.workspace.CreateWorkspaceDto;
import com.roal.survey_engine.domain.survey.dto.workspace.WorkspaceDto;
import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.survey.service.WorkspaceService;
import com.roal.survey_engine.domain.user.entity.UserEntity;
import com.roal.survey_engine.domain.user.repository.UserRepository;
import com.roal.survey_engine.security.AuthenticationFacade;
import org.hashids.Hashids;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.jpa.show-sql=true"})
@Transactional
public class WorkspaceServiceTest {

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("workspaceHashids")
    Hashids workspaceHashid;

    @Autowired
    private EntityManager testEntityManager;

    @MockBean
    AuthenticationFacade authenticationFacade;

    @Test
    public void testUserIsPartOfWorkspace() {
        Workspace workspace = new Workspace().addUser(new UserEntity("user1"));
        var loggedInUser = new org.springframework.security.core.userdetails.User("user1", "pw",
                Set.of(new SimpleGrantedAuthority("USER")));

        given(authenticationFacade.getUserDetails()).willReturn(loggedInUser);

        boolean actual = workspaceService.currentUserCanModifyWorkspace(workspace);

        verify(authenticationFacade, times(1)).getUserDetails();
        assertTrue(actual);
    }

    @Test
    public void testUserIsNotPartOfWorkspace() {
        Workspace workspace = new Workspace().addUser(new UserEntity("user1"));
        var loggedInUser = new org.springframework.security.core.userdetails.User("user2", "pw",
                Set.of(new SimpleGrantedAuthority("USER")));

        given(authenticationFacade.getUserDetails()).willReturn(loggedInUser);

        boolean actual = workspaceService.currentUserCanModifyWorkspace(workspace);

        verify(authenticationFacade, times(1)).getUserDetails();
        assertFalse(actual);
    }

    @Test
    public void testAdminCanEditWorkspace() {
        Workspace workspace = new Workspace().addUser(new UserEntity("user1"));
        var loggedInUser = new org.springframework.security.core.userdetails.User("user2", "pw",
                Set.of(new SimpleGrantedAuthority("ADMIN")));

        var loggedInAuth = new UsernamePasswordAuthenticationToken(loggedInUser, null);

        given(authenticationFacade.getUserDetails()).willReturn(loggedInUser);
        given(authenticationFacade.getAuthentication()).willReturn(loggedInAuth);

        boolean actual = workspaceService.currentUserCanModifyWorkspace(workspace);

        verify(authenticationFacade, times(1)).getUserDetails();
        assertFalse(actual);
    }

    @Test
    public void testGetWorkspacesForCurrentUser() {
        UserEntity user = new UserEntity("user1", "password", "email@test.de", Collections.emptySet());
        UserEntity savedUser = userRepository.save(user);
        Workspace firstWorkspace = new Workspace("first workspace").addUser(savedUser);
        Workspace secondWorkspace = new Workspace("second workspace").addUser(savedUser);
        workspaceRepository.save(firstWorkspace);
        workspaceRepository.save(secondWorkspace);

        given(authenticationFacade.getUserDetails())
                .willReturn(new org.springframework.security.core.userdetails.User("user1", "password", Collections.emptyList()));

        List<Workspace> workspacesForCurrentUser = workspaceService.getWorkspacesForCurrentUser();

        assertEquals(2, workspacesForCurrentUser.size());
        assertEquals("first workspace", workspacesForCurrentUser.get(0).getTitle());
        assertEquals("second workspace", workspacesForCurrentUser.get(1).getTitle());
    }

    @Test
    void create() {
        var createWorkspaceDto = new CreateWorkspaceDto("Title", Collections.emptyList());

        given(authenticationFacade.isAdmin()).willReturn(true);

        WorkspaceDto workspaceDto = workspaceService.create(createWorkspaceDto);

        assertEquals(createWorkspaceDto.title(), workspaceDto.title());

    }

    @Test
    void deleteById() {

        Workspace workspace = workspaceRepository.save(new Workspace("Title"));
        String hashid = workspaceHashid.encode(workspace.getId());

        given(authenticationFacade.isAdmin()).willReturn(true);

        workspaceService.deleteById(hashid);

        testEntityManager.flush();
        testEntityManager.clear();

        Optional<Workspace> byId = workspaceRepository.findById(workspace.getId());

        assertTrue(byId.isEmpty());
    }
}

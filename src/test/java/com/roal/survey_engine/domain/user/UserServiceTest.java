package com.roal.survey_engine.domain.user;

import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.user.dto.UserDto;
import com.roal.survey_engine.domain.user.dto.UserDtoMapper;
import com.roal.survey_engine.domain.user.dto.UserRegistrationDto;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.entity.UserEntity;
import com.roal.survey_engine.domain.user.entity.UserLogin;
import com.roal.survey_engine.domain.user.exception.UserAlreadyExistsException;
import com.roal.survey_engine.domain.user.exception.UserNotFoundException;
import com.roal.survey_engine.domain.user.exception.UserRegistrationValidationException;
import com.roal.survey_engine.domain.user.repository.RoleRepository;
import com.roal.survey_engine.domain.user.repository.UserRepository;
import com.roal.survey_engine.domain.user.service.UserService;
import org.hashids.Hashids;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Spy
    private Hashids userHashids = new Hashids();

    @Spy
    private UserDtoMapper userDtoMapper = new UserDtoMapper(userHashids);

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void testCreateFailedNoIdenticalPassword() {

        UserRegistrationDto registrationDto = new UserRegistrationDto("username",
            "password1", "password2",
            "email@example.de", Set.of(UserAuthority.Constants.USER_VALUE));

        assertThrows(UserRegistrationValidationException.class, () -> userService.create(registrationDto));
    }

    @Test
    void testCreateSuccess() {

        UserRegistrationDto registrationDto = new UserRegistrationDto("username",
            "password1", "password1",
            "email@example.de", Set.of(UserAuthority.Constants.USER_VALUE));

        var userEntity = new UserEntity(registrationDto.username());
        userEntity.setId(1L);
        var role = new Role(UserAuthority.Constants.USER_VALUE);
        userEntity.setRoles(Set.of(role));

        given(userRepository.findUserByUsername("username")).willReturn(Optional.empty());
        given(roleRepository.findAll())
            .willReturn(List.of(role));
        given(userRepository.save(any(UserEntity.class))).willReturn(userEntity);

        UserDto userDto = userService.create(registrationDto);

        assertEquals(registrationDto.username(), userDto.username());

    }

    @Test
    void testCreateFailedUsernameAlreadyExists() {

        UserRegistrationDto registrationDto = new UserRegistrationDto("username",
            "password1", "password1",
            "email@example.de", Set.of(UserAuthority.Constants.USER_VALUE));

        var userEntity = new UserEntity(registrationDto.username());
        userEntity.setId(1L);
        var role = new Role(UserAuthority.Constants.USER_VALUE);
        userEntity.setRoles(Set.of(role));

        given(userRepository.findUserByUsername("username")).willReturn(Optional.of(userEntity));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(registrationDto));

    }

    @Test
    void findByUsernameSuccess() {

        var user = new UserEntity("username");
        user.setRoles(Collections.EMPTY_SET);
        user.setId(1L);

        given(userRepository.findUserByUsername("username")).willReturn(Optional.of(user));

        UserDto dto = userService.findByUsername("username");

        assertEquals(user.getUsername(), dto.username());
    }

    @Test
    void findByUsernameNotFound() {

        given(userRepository.findUserByUsername("username")).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername("username"));

    }

    @Test
    void updateLastLoginByUsername() {

        var user = new UserEntity("username");
        given(userRepository.findUserByUsername("username")).willReturn(Optional.of(user));

        UserLogin userLogin = user.getUserLogin();
        userService.updateLastLoginByUsername("username", "IP1");

        UserLogin userLogin1 = user.getUserLogin();

        userService.updateLastLoginByUsername("username", "IP2");

        UserLogin userLogin2 = user.getUserLogin();

        assertNull(userLogin);
        assertTrue(userLogin2.getDateTime().isAfter(userLogin1.getDateTime()));
        assertEquals("IP1", userLogin1.getIp());
        assertEquals("IP2", userLogin2.getIp());

    }

    @Test
    void findByIdSuccess() {
        String hashid = userHashids.encode(1L);

        var user = new UserEntity("username");
        user.setRoles(Collections.EMPTY_SET);
        user.setId(1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserEntity byId = userService.findById(hashid);

        assertEquals(user.getUsername(), byId.getUsername());
        assertEquals(user.getId(), byId.getId());

    }

    @Test
    void findByIdNotFound() {
        String hashid = userHashids.encode(1L);

        var user = new UserEntity("username");
        user.setRoles(Collections.EMPTY_SET);
        user.setId(1L);

        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(hashid));

    }
}

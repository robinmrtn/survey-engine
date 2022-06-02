package com.roal.survey_engine.domain.user.service;

import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.user.dto.UserDto;
import com.roal.survey_engine.domain.user.dto.UserDtoMapper;
import com.roal.survey_engine.domain.user.dto.UserRegistrationDto;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.entity.UserEntity;
import com.roal.survey_engine.domain.user.exception.RoleNotFoundException;
import com.roal.survey_engine.domain.user.exception.UserAlreadyExistsException;
import com.roal.survey_engine.domain.user.exception.UserNotFoundException;
import com.roal.survey_engine.domain.user.exception.UserRegistrationValidationException;
import com.roal.survey_engine.domain.user.repository.RoleRepository;
import com.roal.survey_engine.domain.user.repository.UserRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final RoleRepository roleRepository;
    private final WorkspaceRepository workspaceRepository;
    private final PasswordEncoder passwordEncoder;
    private final Hashids userHashids;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper,
                       RoleRepository roleRepository, WorkspaceRepository workspaceRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       @Qualifier("userHashids") Hashids userHashids) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.roleRepository = roleRepository;
        this.workspaceRepository = workspaceRepository;
        this.passwordEncoder = passwordEncoder;
        this.userHashids = userHashids;
    }

    @Transactional
    public UserDto create(UserRegistrationDto userDto) {

        if (!userDto.password().equals(userDto.passwordRepeated())) {
            throw new UserRegistrationValidationException("Supplied passwords are not identical");
        }

        userRepository.findUserByUsername(userDto.username())
            .ifPresent((s) -> {
                throw new UserAlreadyExistsException(userDto.username());
            });

        UserEntity user = userDtoMapper.dtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> foundRoles = getRoles(user);
        user.setRoles(foundRoles);
        UserEntity saved = userRepository.save(user);

        /*
            Add default Workspace for new users
         */
        createDefaultWorkspace(saved);

        return userDtoMapper.entityToDto(saved);
    }

    private Set<Role> getRoles(UserEntity user) {

        List<String> allRoles = roleRepository.findAll().stream().map(Role::getName).toList();
        Set<Role> result = new HashSet<>();
        for (Role role : user.getRoles()) {
            if (!allRoles.contains(role.getName())) {
                throw new RoleNotFoundException(role.getName());
            }
            result.add(role);

        }
        return result;
    }

    private void createDefaultWorkspace(UserEntity user) {
        var workspace = new Workspace(user.getUsername() + " Workspace");
        workspace.addUser(user);
        workspaceRepository.save(workspace);
    }

    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        UserEntity user = userRepository.findUserByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Username '" + username + "' not found"));
        return userDtoMapper.entityToDto(user);
    }

    @Transactional
    public void updateLastLoginByUsername(String username, String ip) {
        UserEntity user = userRepository.findUserByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Username '" + username + "' not found"));

        user.updateLastLogin(ip);
    }

    @Transactional(readOnly = true)
    public UserEntity findById(String hashid) {
        long id = hashidToId(hashid);
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(hashid));
    }

    private long hashidToId(String hashid) {
        long[] decode = userHashids.decode(hashid);
        if (decode.length == 0) {
            throw new UserNotFoundException(hashid);
        }
        return decode[0];
    }

    public String idToHash(long id) {
        return userHashids.encode(id);
    }
}

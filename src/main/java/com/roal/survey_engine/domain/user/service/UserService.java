package com.roal.survey_engine.domain.user.service;

import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.user.dto.UserDto;
import com.roal.survey_engine.domain.user.dto.UserDtoMapper;
import com.roal.survey_engine.domain.user.dto.UserRegistrationDto;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.entity.UserEntity;
import com.roal.survey_engine.domain.user.exception.RoleNotFoundException;
import com.roal.survey_engine.domain.user.exception.UserNotFoundException;
import com.roal.survey_engine.domain.user.repository.RoleRepository;
import com.roal.survey_engine.domain.user.repository.UserRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final RoleRepository roleRepository;
    private final WorkspaceRepository workspaceRepository;
    private final PasswordEncoder passwordEncoder;
    private final Hashids userHashids;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper,
                       RoleRepository roleRepository, WorkspaceRepository workspaceRepository,
                       PasswordEncoder passwordEncoder,
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

        UserEntity user = userDtoMapper.dtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> foundRoles = getRoles(user);
        user.setRoles(foundRoles);
        UserEntity saved = userRepository.save(user);

        /*
            Add default Workspace for new users
         */
        createDefaultWorkspace(user);

        return userDtoMapper.entityToDto(saved);
    }

    private Set<Role> getRoles(UserEntity user) {
        return user
                .getRoles()
                .stream()
                .map((r) -> roleRepository.findByName(r.getName())
                        .orElseThrow(() -> new RoleNotFoundException(r.getName())))
                .collect(Collectors.toSet());
    }

    private void createDefaultWorkspace(UserEntity user) {
        var workspace = new Workspace(user.getUsername() + " Workspace");
        workspace.addUser(user);
        workspaceRepository.save(workspace);
    }

    public UserDto findByUsername(String username) {
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username '" + username + "' not found"));
        return userDtoMapper.entityToDto(user);
    }

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

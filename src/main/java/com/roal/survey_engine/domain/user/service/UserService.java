package com.roal.survey_engine.domain.user.service;

import com.roal.survey_engine.domain.user.dto.UserDto;
import com.roal.survey_engine.domain.user.dto.UserDtoMapper;
import com.roal.survey_engine.domain.user.dto.UserRegistrationDto;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.entity.User;
import com.roal.survey_engine.domain.user.exception.RoleNotFoundException;
import com.roal.survey_engine.domain.user.repository.RoleRepository;
import com.roal.survey_engine.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto save(UserRegistrationDto userDto) {

        User user = userDtoMapper.dtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> foundRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            foundRoles.add(roleRepository.findByName(role.getName())
                    .orElseThrow(() -> new RoleNotFoundException(role.getName()))
            );
        }

        user.setRoles(foundRoles);
        User saved = userRepository.save(user);
        return userDtoMapper.entityToDto(saved);
    }

    public UserDto findByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username '" + username + "' not found"));
        return userDtoMapper.entityToDto(user);
    }


}

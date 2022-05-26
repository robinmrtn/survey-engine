package com.roal.survey_engine.domain.user.dto;

import com.roal.survey_engine.domain.user.UserAuthority;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.entity.UserEntity;
import com.roal.survey_engine.security.AuthenticationFacade;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDtoMapper {

    private final Hashids userHashids;
    private final AuthenticationFacade authenticationFacade;

    public UserDtoMapper(@Qualifier("userHashids") Hashids userHashids, AuthenticationFacade authenticationFacade) {
        this.userHashids = userHashids;
        this.authenticationFacade = authenticationFacade;
    }

    public UserEntity dtoToEntity(UserRegistrationDto userDto) {
        Set<Role> roles = userDto.roles()
                .stream()
                .map(Role::new)
                .collect(Collectors.toSet());
        return new UserEntity(userDto.username(), userDto.password(), roles);
    }

    public UserDto entityToDto(UserEntity user) {
        Set<String> roles = user.getRoles()
            .stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        return new UserDto(userHashids.encode(user.getId()),
            user.getUsername(), roles, isAdmin(user.getRoles()));
    }

    private boolean isAdmin(Set<Role> roles) {
        return roles.stream()
            .anyMatch(role -> role.getName().equals(UserAuthority.ROLE_ADMIN.getRole()));
    }
}

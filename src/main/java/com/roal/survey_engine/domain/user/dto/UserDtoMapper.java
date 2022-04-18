package com.roal.survey_engine.domain.user.dto;

import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.entity.User;
import com.roal.survey_engine.security.AuthenticationFacade;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public User dtoToEntity(UserRegistrationDto userDto) {
        List<Role> roles = userDto.roles()
            .stream()
            .map(Role::new)
            .toList();
        return new User(userDto.username(), userDto.password(), roles);
    }

    public UserDto entityToDto(User user) {
        Set<String> roles = user.getRoles()
            .stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        return new UserDto(userHashids.encode(user.getId()),
            user.getUsername(), roles, authenticationFacade.isAdmin());
    }
}

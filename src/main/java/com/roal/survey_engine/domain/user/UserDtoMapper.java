package com.roal.survey_engine.domain.user;

import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.entity.User;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDtoMapper {

    private final Hashids userHashids;

    public UserDtoMapper(@Qualifier("userHashids") Hashids userHashids) {
        this.userHashids = userHashids;
    }

    public User dtoToEntity(UserDto userDto) {
        List<Role> roles = userDto.roles().stream().map(Role::new).toList();
        return new User(userDto.username(), userDto.password(), roles);
    }

    public UserDto entityToDto(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new UserDto(userHashids.encode(user.getId()),
                user.getUsername(), null, roles);
    }
}

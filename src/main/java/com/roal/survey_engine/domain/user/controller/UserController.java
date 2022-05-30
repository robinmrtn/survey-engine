package com.roal.survey_engine.domain.user.controller;

import com.roal.survey_engine.domain.user.UserAuthority;
import com.roal.survey_engine.domain.user.dto.UserDto;
import com.roal.survey_engine.domain.user.dto.UserRegistrationDto;
import com.roal.survey_engine.domain.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDto me(Principal principal) {
        return userService.findByUsername(principal.getName());
    }

    @RolesAllowed({UserAuthority.Constants.ADMIN_VALUE})
    @PostMapping()
    public UserDto create(@RequestBody @Valid UserRegistrationDto userRegistration) {
        return userService.create(userRegistration);
    }

}

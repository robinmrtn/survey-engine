package com.roal.survey_engine.domain.user.controller;

import com.roal.survey_engine.domain.user.dto.UserDto;
import com.roal.survey_engine.domain.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.UserRegistrationRequest;
import com.whatsnext.backend.dto.UserResponse;
import com.whatsnext.backend.service.AppUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public UserResponse registrar(@RequestBody UserRegistrationRequest request){
        return appUserService.registrar(request);
    }
}

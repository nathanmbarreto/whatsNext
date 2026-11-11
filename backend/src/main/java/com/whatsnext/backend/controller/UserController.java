package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.UserRegistrationRequest;
import com.whatsnext.backend.dto.UserResponse;
import com.whatsnext.backend.service.AppUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public UserResponse usuarioAtual(Authentication authentication) {
        return appUserService.buscarUsuarioAtual(authentication.getName());
    }
}

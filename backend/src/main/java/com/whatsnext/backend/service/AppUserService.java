package com.whatsnext.backend.service;

import com.whatsnext.backend.dto.UserRegistrationRequest;
import com.whatsnext.backend.dto.UserResponse;
import com.whatsnext.backend.entity.AppUser;
import com.whatsnext.backend.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registrar(UserRegistrationRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request não pode ser nulo ou em branco.");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome não pode ser nulo ou em branco.");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email não pode ser nulos ou em branco.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passowrd não podem ser nulos ou em branco.");
        }


        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email já registrado"
            );
        }

        AppUser novoUsuario = new AppUser(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        AppUser usuarioSalvo = appUserRepository.save(novoUsuario);

        UserResponse usuario = new UserResponse();
        usuario.setId(usuarioSalvo.getId());
        usuario.setEmail(usuarioSalvo.getEmail());
        usuario.setName(usuarioSalvo.getName());

        return usuario;

    }
}

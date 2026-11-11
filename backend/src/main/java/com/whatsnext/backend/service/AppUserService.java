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

import java.util.Optional;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser buscarOuCriarPorGoogle(String googleId, String email, String name) {

        if (googleId == null || googleId.isBlank()
                || email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "O ID Google e o e-mail são obrigatórios.");
        }

        String nome = name == null || name.isBlank() ? email : name;

        Optional<AppUser> porGoogleId =
                appUserRepository.findByGoogleId(googleId);

        if (porGoogleId.isPresent()) {
            AppUser usuario = porGoogleId.get();

            Optional<AppUser> donoDoEmail =
                    appUserRepository.findByEmail(email);

            if (donoDoEmail.isPresent()
                    && !donoDoEmail.get().getId().equals(usuario.getId())) {
                throw new IllegalStateException(
                        "Este e-mail já pertence a outra conta.");
            }

            usuario.setEmail(email);
            usuario.setName(nome);
            return appUserRepository.save(usuario);
        }

        Optional<AppUser> porEmail =
                appUserRepository.findByEmail(email);

        if (porEmail.isPresent()) {
            AppUser usuario = porEmail.get();

            if (usuario.getGoogleId() != null
                    && !usuario.getGoogleId().equals(googleId)) {
                throw new IllegalStateException(
                        "Este e-mail já está associado a outra conta Google.");
            }

            usuario.setGoogleId(googleId);
            usuario.setName(nome);
            return appUserRepository.save(usuario);
        }

        AppUser novoUsuario = new AppUser(nome, email, null);
        novoUsuario.setGoogleId(googleId);

        return appUserRepository.save(novoUsuario);
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

    @Transactional(readOnly = true)
    public UserResponse buscarUsuarioAtual(String email) {
        AppUser usuario = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado."
                ));

        UserResponse resposta = new UserResponse();
        resposta.setId(usuario.getId());
        resposta.setName(usuario.getName());
        resposta.setEmail(usuario.getEmail());

        return resposta;
    }
}

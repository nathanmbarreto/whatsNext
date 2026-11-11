package com.whatsnext.backend.service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class GoogleOidcUserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final AppUserService appUserService;
    private final OidcUserService delegate = new OidcUserService();

    public GoogleOidcUserService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest request)
            throws OAuth2AuthenticationException {
        OidcUser googleUser = delegate.loadUser(request);

        String googleId = googleUser.getSubject();
        String email = googleUser.getClaimAsString("email");
        String name = googleUser.getClaimAsString("name");
        Boolean emailVerified = googleUser.getClaimAsBoolean("email_verified");

        if (email == null || email.isBlank()
                || !Boolean.TRUE.equals(emailVerified)) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_user_info"),
                    "O Google não forneceu um e-mail verificado."
            );
        }

        appUserService.buscarOuCriarPorGoogle(googleId, email, name);

        return new DefaultOidcUser(
                googleUser.getAuthorities(),
                googleUser.getIdToken(),
                googleUser.getUserInfo(),
                "email"
        );
    }
}
package io.github.leocklaus.template_project_with_jwt_security_and_activation.api.controllers;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.*;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.config.security.TokenService;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.User;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController implements IAuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserOutput> register(@RequestBody @Valid UserInput userInput) {
        UserOutput user = userService.addUser(userInput);
        URI uri = URI.create("/user/" + user.id());
        return ResponseEntity
                .ok(user);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<TokenOutput> login(@RequestBody @Valid Login login){
        var usernamePassword = new UsernamePasswordAuthenticationToken(login.username(), login.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new TokenOutput((token)));
    }

    @Override
    @PostMapping("/changepassword")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordInput input){
        userService.changePassword(input);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/activate/{code}")
    public ResponseEntity<Void> changePassword(@PathVariable String code){
        userService.enableUser(code);
        return ResponseEntity.noContent().build();
    }

}

package io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record Login(
        @NotNull(message = "Username is required") String username,
        @NotNull(message = "Password is required") String password
) {
}

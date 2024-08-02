package io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserInput(
        @NotNull(message = "Username is required") String username,
        @NotNull(message = "Email is required") @Email String email,
        @NotNull(message = "Password is required") String password
) {
}

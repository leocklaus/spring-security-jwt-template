package io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto;

import jakarta.validation.constraints.NotNull;

public record ChangePasswordInput(
        @NotNull(message = "Password is required") String currentPassword,
        @NotNull(message = "Password is required") String newPassword
) {
}

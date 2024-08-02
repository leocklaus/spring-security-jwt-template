package io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.User;

import java.util.UUID;

public record UserOutput(
        UUID id,
        String username,
        String email
) {
    public UserOutput(User user){
        this(user.getId(), user.getUsername(), user.getEmail());
    }
}

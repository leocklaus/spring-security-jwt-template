package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends UserException{
    public UserNotFoundException(String msg) {
        super(msg);
    }
    public UserNotFoundException(UUID uuid){
        this("User not found with id: " + uuid);
    }
}

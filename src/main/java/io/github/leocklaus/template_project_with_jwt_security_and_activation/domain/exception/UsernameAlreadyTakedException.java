package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception;

public class UsernameAlreadyTakedException extends UserException{
    public UsernameAlreadyTakedException(String username) {
        super(username + "has already been taken");
    }
}

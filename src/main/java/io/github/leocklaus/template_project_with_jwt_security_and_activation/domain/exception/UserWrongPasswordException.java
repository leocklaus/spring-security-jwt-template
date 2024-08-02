package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception;

public class UserWrongPasswordException extends UserException{
    public UserWrongPasswordException() {
        super("Password do not match");
    }
}

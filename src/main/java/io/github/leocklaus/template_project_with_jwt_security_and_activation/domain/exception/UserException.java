package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception;

public class UserException extends RuntimeException{
    public UserException(String msg){
        super(msg);
    }
}

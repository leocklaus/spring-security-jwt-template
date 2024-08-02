package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception;

public class InvalidCodeException extends RuntimeException{
    public InvalidCodeException(String msg){
        super(msg);
    }
}

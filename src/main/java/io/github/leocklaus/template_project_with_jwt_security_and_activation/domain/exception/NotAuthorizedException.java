package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException(String msg){
        super(msg);
    }
}

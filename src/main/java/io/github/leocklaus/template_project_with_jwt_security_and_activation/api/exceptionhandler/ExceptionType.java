package io.github.leocklaus.template_project_with_jwt_security_and_activation.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionType {

    USER_NOT_FOUND("Usuário não encontrado", "/user-not-found"),
    INVALID_DATA("Dados inválidos", "/invalid-data"),
    RESOURCE_NOT_FOUND("Recurso não encontrado", "/resource-not-found"),
    WRONG_PASSWORD("A senha digitada é incorreta", "/wrong-password"),
    NOT_AUTHORIZED("Requisição não autorizada ou credenciais inválidas", "/not-authorized"),
    USER_DISABLED("Ative o usuário antes de fazer login", "/user-disabled"),
    BAD_CREDENTIALS("Usuário e/ou senha inválidos", "/bad-credentials"),
    USERNAME_ALREADY_TAKEN("O usuário já está em uso", "/user-already-taken"),
    INVALID_CODE_EXCEPTION("Código inválido ou expirado", "/invalid-code");

    private final String title;
    private final String URI;

    ExceptionType(String title, String URI){
        this.title = title;
        this.URI = URI;
    }
}

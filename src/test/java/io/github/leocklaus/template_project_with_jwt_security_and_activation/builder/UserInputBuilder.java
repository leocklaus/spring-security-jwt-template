package io.github.leocklaus.template_project_with_jwt_security_and_activation.builder;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.UserInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserInputBuilder {
    private String username = "username";
    private String email = "email@email.com";
    private String password = "123456";

    public UserInputBuilder withUsername(String username){
        this.username = username;
        return this;
    }

    public UserInputBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public UserInputBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public UserInput build(){
        return new UserInput(
                username,
                email,
                password
        );
    }
}

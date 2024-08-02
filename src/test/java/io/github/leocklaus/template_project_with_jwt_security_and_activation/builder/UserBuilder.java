package io.github.leocklaus.template_project_with_jwt_security_and_activation.builder;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.User;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.UserRoles;
import jakarta.persistence.Column;

import java.util.UUID;

public class UserBuilder {
    private UUID id = UUID.randomUUID();

    private String username = "username";
    private String email = "email@email.com";
    private String password = "123456";
    private UserRoles roles = UserRoles.USER;
    private Boolean isEnabled = false;

    public UserBuilder withId(UUID id){
        this.id = id;
        return this;
    }

    public UserBuilder withUsername(String username){
        this.username = username;
        return this;
    }

    public UserBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public UserBuilder withRole(UserRoles roles){
        this.roles = roles;
        return this;
    }

    public UserBuilder asEnabled(){
        this.isEnabled = true;
        return this;
    }

    public User build(){
        return new User(
                id,
                username,
                email,
                password,
                roles,
                isEnabled
        );
    }
}

package io.github.leocklaus.template_project_with_jwt_security_and_activation.builder;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.Code;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.CodeType;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

public class CodeBuilder {

    private UUID id = UUID.randomUUID();
    private String code;
    private CodeType codeType = CodeType.ACTIVATION;
    private User user = new UserBuilder().build();
    private final Instant createAt = Instant.now();
    private Instant validUntil;

    public CodeBuilder withId(UUID id){
        this.id = id;
        return this;
    }

    public CodeBuilder withCode(String code){
        this.code = code;
        return this;
    }

    public CodeBuilder withCodeType(CodeType code){
        this.codeType = code;
        return this;
    }

    public CodeBuilder withUser(User user){
        this.user = user;
        return this;
    }

    public CodeBuilder withValidUntil(Instant instant){
        this.validUntil = instant;
        return this;
    }

    public Code build(){
        return new Code(
                id,
                code,
                codeType,
                user,
                createAt,
                validUntil
        );
    }
}

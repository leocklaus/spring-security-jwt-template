package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.repository;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CodeRepository extends JpaRepository<Code, UUID> {
    Optional<Code> findByCode(String code);
}

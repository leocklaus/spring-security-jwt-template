package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.repository;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    UserDetails findByUsername(String username);
    @Query("SELECT u from User u WHERE u.username = ?1")
    Optional<User> findByUsernameOptional(String username);
    boolean existsByUsername(String username);

}

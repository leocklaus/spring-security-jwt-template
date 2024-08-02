package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private CodeType codeType;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @CreationTimestamp
    private Instant createAt;

    private Instant validUntil;

    public void setFiveMinutesDuration(){
        this.validUntil = Instant.now().plusSeconds(60*5);
    }

    public void setTenMinutesDuration(){
        this.validUntil = Instant.now().plusSeconds(60*10);
    }

    public boolean isExpired(){
        return Instant.now().isAfter(validUntil);
    }

    public void generateCode(int length){
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

         this.code = codeBuilder.toString();
    }
}

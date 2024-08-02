package io.github.leocklaus.template_project_with_jwt_security_and_activation.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secretKey;

    public String generateToken(User user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withIssuer("urlshortener")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException creationException){
            throw new RuntimeException("Error while generating token", creationException);
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT
                    .require(algorithm)
                    .withIssuer("urlshortener")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException verificationException){
            return "";
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}

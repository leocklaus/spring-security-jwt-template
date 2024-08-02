package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.service;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.ChangePasswordInput;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.UserInput;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.UserOutput;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.Code;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.CodeType;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.User;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.UserRoles;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception.InvalidCodeException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception.UserNotFoundException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception.UserWrongPasswordException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception.UsernameAlreadyTakedException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.repository.CodeRepository;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final CodeRepository codeRepository;
    private final EmailService emailService;

    public UserOutput getUserById(UUID id){
        User user = getUserByIdOrThrowsExceptionIfNotExists(id);
        return new UserOutput(user);
    }

    @Transactional
    public UserOutput addUser(UserInput userInput) {

        if(userRepository.existsByUsername(userInput.username())){
            throw new UsernameAlreadyTakedException(userInput.username());
        }

        User user = User.builder()
                .username(userInput.username())
                .password(encodePassword(userInput.password()))
                .email(userInput.email())
                .roles(UserRoles.USER)
                .isEnabled(false)
                .build();

        user = userRepository.save(user);

        sendCodeEmail(CodeType.ACTIVATION, user);

        return new UserOutput(user);
    }

    public void enableUser(String activationCode) {
        Code code = codeRepository.findByCode(activationCode)
                .orElseThrow(() -> new InvalidCodeException("Invalid activation code"));

        var user = code.getUser();

        if(code.isExpired() && !user.isEnabled()){
            sendCodeEmail(CodeType.ACTIVATION, user);
            throw new InvalidCodeException("Code has expired");
        }

        user.enableAccount();

        userRepository.save(user);
    }

    public void sendCodeEmail(CodeType type, User user) {

        var code = generateAndSaveCode(type, user);

        try{
            emailService.sendCodeEmail(user.getEmail(), user.getUsername(), type, code);
        }catch (Exception e){
            log.error(e.getMessage());
        }


    }

    private Code generateAndSaveCode(CodeType type, User user){
        Code code = Code.builder()
                .user(user)
                .codeType(type)
                .build();

        code.setFiveMinutesDuration();
        code.generateCode(5);

        return codeRepository.save(code);
    }

    @Transactional
    public void changePassword(ChangePasswordInput input){
        User user = getLoggedUserOrThrowsExceptionIfNotExists();

        if(!passwordMatches(input.currentPassword(), user.getPassword())){
            throw new UserWrongPasswordException();
        };

        user.setPassword(encodePassword(input.newPassword()));

        userRepository.save(user);
    }

    private static boolean passwordMatches(String currentRawPassword, String currentEncryptedPassword) {
        return new BCryptPasswordEncoder().matches(currentRawPassword, currentEncryptedPassword);
    }

    private String encodePassword(String password){
        return new BCryptPasswordEncoder().encode(password);
    }

    public User getLoggedUserOrThrowsExceptionIfNotExists(){
        String authenticatedUsername = authService.getAuthenticatedUsername();
        Optional<User> user = userRepository.findByUsernameOptional(authenticatedUsername);

        if(user.isEmpty()){
            throw new UserNotFoundException("User not found with username: " + authenticatedUsername);
        }

        return user.get();
    }

    public User getUserByIdOrThrowsExceptionIfNotExists(UUID id){
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
    }

}

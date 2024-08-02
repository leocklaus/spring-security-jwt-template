package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.service;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.ChangePasswordInput;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.UserInput;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.UserOutput;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.builder.CodeBuilder;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.builder.UserBuilder;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.builder.UserInputBuilder;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.Code;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.CodeType;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.User;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception.InvalidCodeException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception.UserNotFoundException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception.UserWrongPasswordException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.exception.UsernameAlreadyTakedException;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.repository.CodeRepository;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AuthService authService;

    @Mock
    EmailService emailService;

    @Mock
    CodeRepository codeRepository;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    ArgumentCaptor<CodeType> codeTypeArgumentCaptor;

    @Spy
    @InjectMocks
    UserService userService;

    @Nested
    class GetUserById{

        @Test
        void getUserByIdShouldReturnUserOutput() {

            User user = new UserBuilder()
                    .asEnabled()
                    .build();

            UserOutput output = new UserOutput(user);

            doReturn(user).when(userService)
                    .getUserByIdOrThrowsExceptionIfNotExists(uuidArgumentCaptor.capture());

            var response = userService.getUserById(user.getId());

            assertEquals(user.getId(), uuidArgumentCaptor.getValue());
            assertEquals(output, response);
            verify(userService, times(1)).getUserById(any());
        }
    }


    @Nested
    class AddUser{

        @Test
        void shouldThrowExceptionIFUsernameAlreadyExists() {
            UserInput userInput = new UserInputBuilder()
                    .build();

            doReturn(true).when(userRepository).existsByUsername(userInput.username());

            assertThrows(UsernameAlreadyTakedException.class, ()->{
                userService.addUser(userInput);
            });
        }

        @Test
        void shouldBuildUserEntityCorrectly() {
            UserInput userInput = new UserInputBuilder()
                    .build();

            User user = new UserBuilder()
                    .withUsername(userInput.username())
                    .withEmail(userInput.email())
                    .withPassword(userInput.password())
                    .build();

            doReturn(false).when(userRepository).existsByUsername(userInput.username());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            userService.addUser(userInput);

            assertEquals(userInput.username(), userArgumentCaptor.getValue().getUsername());
            assertEquals(userInput.email(), userArgumentCaptor.getValue().getEmail());
            assertNotEquals(userInput.password(), userArgumentCaptor.getValue().getPassword());

        }

        @Test
        void shouldReturnOutputDTO() {
            UserInput userInput = new UserInputBuilder()
                    .build();

            User user = new UserBuilder()
                    .withUsername(userInput.username())
                    .withEmail(userInput.email())
                    .withPassword(userInput.password())
                    .build();

            var output = new UserOutput(user);

            doReturn(false).when(userRepository).existsByUsername(userInput.username());
            doReturn(user).when(userRepository).save(any());

            var response = userService.addUser(userInput);

            assertEquals(output.username(), response.username());
            assertEquals(output.email(), response.email());

        }

        @Test
        void shouldSendEmail() {
            UserInput userInput = new UserInputBuilder()
                    .build();

            User user = new UserBuilder()
                    .withUsername(userInput.username())
                    .withEmail(userInput.email())
                    .withPassword(userInput.password())
                    .build();

            doReturn(false).when(userRepository).existsByUsername(userInput.username());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            doNothing().when(userService).sendCodeEmail(
                    codeTypeArgumentCaptor.capture(), userArgumentCaptor.capture());

            userService.addUser(userInput);

            assertEquals(CodeType.ACTIVATION, codeTypeArgumentCaptor.getValue());
            assertEquals(userInput.email(), userArgumentCaptor.getValue().getEmail());
            verify(userService, times(1)).sendCodeEmail(any(), any());
        }

    }

    @Nested
    class EnableUser{
        @Test
        void shouldThrowExceptionIfInvalidCode() {

            String activationCodeInput = "123";

            Code code = new CodeBuilder()
                    .build();

            code.generateCode(5);
            code.setFiveMinutesDuration();

            doReturn(Optional.empty()).when(codeRepository).findByCode(activationCodeInput);

            assertThrows(InvalidCodeException.class, ()->{
                userService.enableUser(activationCodeInput);
            });

        }

        @Test
        void shouldThrowExceptionIfCodeHasExpired() {
            String activationCodeInput = "123456";

            Code code = new CodeBuilder()
                    .withCode(activationCodeInput)
                    .withValidUntil(Instant.now().minusSeconds(100))
                    .build();

            doReturn(Optional.of(code)).when(codeRepository).findByCode(activationCodeInput);

            assertThrows(InvalidCodeException.class, ()->{
                userService.enableUser(activationCodeInput);
            });
        }

        @Test
        void shouldSendNewEmailIfCodeHasExpired() throws MessagingException {
            String activationCodeInput = "123456";

            Code code = new CodeBuilder()
                    .withCode(activationCodeInput)
                    .withValidUntil(Instant.now().minusSeconds(100))
                    .build();

            doReturn(Optional.of(code)).when(codeRepository).findByCode(activationCodeInput);

            assertThrows(InvalidCodeException.class, ()->{
                userService.enableUser(activationCodeInput);
            });

            verify(userService, times(1)).sendCodeEmail(any(), any());
        }

        @Test
        void shouldEnableAccountIfValidCodeAndNotExpired() {
            String activationCodeInput = "123456";

            Code code = new CodeBuilder()
                    .withCode(activationCodeInput)
                    .build();

            code.setFiveMinutesDuration();

            doReturn(Optional.of(code)).when(codeRepository).findByCode(activationCodeInput);

            userService.enableUser(activationCodeInput);

            verify(userRepository, times(1)).save(any());
        }

    }


    @Nested
    class ChangePassword{
        @Test
        void shouldThrowExceptionIfPasswordNotMatching() {

            ChangePasswordInput input = new ChangePasswordInput("123456", "789987");

            User user = new UserBuilder()
                    .withPassword("abcdef")
                    .build();

            doReturn(user).when(userService).getLoggedUserOrThrowsExceptionIfNotExists();

            assertThrows(UserWrongPasswordException.class, ()->{
                userService.changePassword(input);
            });

        }

        @Test
        void shouldChangePasswordIfMatching() {
            ChangePasswordInput input = new ChangePasswordInput("123456", "789987");

            User user = new UserBuilder()
                    .withPassword(new BCryptPasswordEncoder().encode("123456"))
                    .build();

            doReturn(user).when(userService).getLoggedUserOrThrowsExceptionIfNotExists();

            userService.changePassword(input);

            verify(userRepository, times(1)).save(any());
        }
    }

    @Nested
    class GetLoggedUserOrThrowsException{
        @Test
        void shouldThrowExceptionIfNotExists() {

            doReturn("username").when(authService).getAuthenticatedUsername();
            doReturn(Optional.empty()).when(userRepository).findByUsernameOptional("username");

            assertThrows(UserNotFoundException.class, ()->{
                userService.getLoggedUserOrThrowsExceptionIfNotExists();
            });
        }
    }

    @Nested
    class GetUserByIdOrThrowsException{
        @Test
        void getUserByIdOrThrowsExceptionIfNotExists() {

            doReturn(Optional.empty()).when(userRepository).findById(any());

            assertThrows(UserNotFoundException.class, ()->{
                userService.getUserByIdOrThrowsExceptionIfNotExists(any());
            });
        }
    }

}
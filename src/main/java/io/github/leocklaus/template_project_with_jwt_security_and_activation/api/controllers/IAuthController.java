package io.github.leocklaus.template_project_with_jwt_security_and_activation.api.controllers;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.dto.*;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.api.exceptionhandler.ExceptionModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth Controller", description = "Endpoints to create and login users")
public interface IAuthController {

    @Operation(
            summary = "Register a new User",
            description = "A new user will be created with correct info provided. Email and Username should be unique."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "User created",
                            responseCode = "201",
                            headers = @Header(
                                    name = "Location",
                                    description = "Location of new User"
                            ),
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = UserOutput.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Email Already Taken Exception",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Username Already Taken Exception",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "One ore more input fields are invalid",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    )
            }
    )
    ResponseEntity<UserOutput> register(@RequestBody @Valid UserInput userInput);

    @Operation(
            summary = "User Login",
            description = "If credentials are correct, login will return a JWT token."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "Login",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = TokenOutput.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Username and/or password are not correct",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User account is not enabled yet",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "One ore more input fields are invalid",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    )
            }
    )
    ResponseEntity<TokenOutput> login(@RequestBody @Valid Login login);

    @Operation(
            summary = "Change user password",
            description = "User password will be changed if current password is correct"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "Password Changed",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Incorrect Password",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "One ore more input fields are invalid",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordInput input);

    @Operation(
            summary = "Activate user account",
            description = "Account will be activate is code is valid"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "Account Activated",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid code",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionModel.class
                                    )
                            )
                    )
            }
    )
    @PostMapping("/activate/{code}")
    ResponseEntity<Void> changePassword(@PathVariable String code);
}

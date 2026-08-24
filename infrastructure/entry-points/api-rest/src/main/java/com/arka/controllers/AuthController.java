package com.arka.controllers;

import com.arka.docs.CommonApiResponses;
import com.arka.dto.input.PasswordResetInput;
import com.arka.dto.output.AuthLoginOutput;
import com.arka.dto.output.AuthRegisterOutput;
import com.arka.mapper.AuthRestMapper;
import com.arka.mapper.UserRestMapper;
import com.arka.request.ForgotPasswordRequest;
import com.arka.request.PasswordResetRequest;
import com.arka.request.UserLoginRequest;
import com.arka.request.UserRegisterRequest;
import com.arka.response.AppResponse;
import com.arka.response.AuthLoginResponse;
import com.arka.response.AuthRegisterResponse;
import com.arka.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",
        description = "Endpoints for user registration, authentication, and password management")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final PasswordResetUseCase passwordResetUseCase;

    private final UserRestMapper userMapper;
    private final AuthRestMapper authMapper;
    private final SendPasswordResetTokenEmailUseCase sendPasswordResetTokenEmailUseCase;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user account with default client permissions."
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRegisterResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload or validation constraint failure",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppResponse.class)
                    ))
    })
    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {

        AuthRegisterOutput authOutput =
                registerUserUseCase.execute(userMapper.toInput(request));

        AuthRegisterResponse response = authMapper.toResponse(authOutput);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.user().id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Operation(
            summary = "User login",
            description = "Authenticates user credentials and returns JWT bearer tokens."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthLoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials or malformed request payload",
                    content = @Content(schema = @Schema(implementation = AppResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during authentication",
                    content = @Content(schema = @Schema(implementation = AppResponse.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {

        AuthLoginOutput authOutput =
                loginUserUseCase.execute(userMapper.toInput(request));

        return ResponseEntity.ok(authMapper.toResponse(authOutput));
    }

    @Operation(
            summary = "Request password reset email",
            description = "Generates a reset token and sends an email instruction link if the address exists."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset email dispatched",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid email format or request payload",
                    content = @Content(schema = @Schema(implementation = AppResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error or email service failure",
                    content = @Content(schema = @Schema(implementation = AppResponse.class))
            )
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<AppResponse<Void>> sendEmailPasswordResetToken(
            @Valid @RequestBody ForgotPasswordRequest request) {

        sendPasswordResetTokenEmailUseCase.execute(request.email());

        return ResponseEntity.ok(AppResponse.success(
                "PASSWORD_RESET_TOKEN_SENT",
                "Password reset token sent successfully"));
    }

    @Operation(
            summary = "Reset user password",
            description = "Updates user password using a valid, non-expired reset token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Password successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid or expired reset token, or weak password provided",
                    content = @Content(schema = @Schema(implementation = AppResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during password reset process",
                    content = @Content(schema = @Schema(implementation = AppResponse.class))
            )
    })
    @PostMapping("/reset-password")
    public ResponseEntity<AppResponse<Void>> resetPassword(
            @Valid @RequestBody PasswordResetRequest request) {

        passwordResetUseCase.execute(
                new PasswordResetInput(request.newPassword(), request.token()));

        return ResponseEntity.ok(AppResponse.success(
                "PASSWORD_CHANGED",
                "Password has been changed successfully"));
    }

}

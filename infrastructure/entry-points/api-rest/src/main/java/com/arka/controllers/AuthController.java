package com.arka.controllers;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "auth", description = "Authentication flow")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final PasswordResetUseCase passwordResetUseCase;

    private final UserRestMapper userMapper;
    private final AuthRestMapper authMapper;
    private final SendPasswordResetTokenEmailUseCase sendPasswordResetTokenEmailUseCase;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Users registration")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRegisterResponse.class)
                    )
            )
    })
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

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {

        AuthLoginOutput authOutput =
                loginUserUseCase.execute(userMapper.toInput(request));

        return ResponseEntity.ok(authMapper.toResponse(authOutput));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AppResponse<Void>> sendEmailPasswordResetToken(
            @Valid @RequestBody ForgotPasswordRequest request){

        sendPasswordResetTokenEmailUseCase.execute(request.email());

        return ResponseEntity.ok(AppResponse.success(
                "PASSWORD_RESET_TOKEN_SENT",
                "Password reset token sent successfully"));
    }

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

package com.arka.controllers;

import com.arka.docs.CommonApiResponses;
import com.arka.dto.output.UserOutput;
import com.arka.mapper.UserRestMapper;
import com.arka.request.VerificationCodeRequest;
import com.arka.response.AppResponse;
import com.arka.usecase.FindCurrentUserByEmailUseCase;
import com.arka.response.UserResponse;
import com.arka.usecase.SendVerificationCodeEmailUseCase;
import com.arka.usecase.VerifyUserEmailUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
@Tag(name = "User Management",
        description = "Endpoints for authenticated user profile operations and account verification")
public class UserController {

    private final FindCurrentUserByEmailUseCase findCurrentUserByEmailUseCase;
    private final UserRestMapper userMapper;
    private final SendVerificationCodeEmailUseCase sendVerificationCodeEmailUseCase;
    private final VerifyUserEmailUseCase verifyUserEmailUseCase;

    @Operation(
            summary = "Get current authenticated user profile",
            description = "Retrieves profile information for the user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User profile retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    @CommonApiResponses
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {

        String email = authentication.getName();
        UserOutput user = findCurrentUserByEmailUseCase.execute(email);

        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @Operation(
            summary = "Request email verification code",
            description = "Generates and dispatches a verification code email to the authenticated user's address."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Verification code dispatched successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppResponse.class)
            )
    )
    @CommonApiResponses
    @PostMapping("/verify-email/request")
    public ResponseEntity<AppResponse<Void>> sendEmailVerificationCode(Authentication authentication) {

        sendVerificationCodeEmailUseCase.execute(authentication.getName());

        return ResponseEntity.ok(AppResponse.success("VERIFICATION_CODE_SENT",
                "Email verification code sent successfully"));
    }

    @Operation(
            summary = "Verify user email address",
            description = "Validates the verification code submitted by " +
                    "the user and updates their account status to verified."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User email verified successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppResponse.class)
            )
    )
    @CommonApiResponses
    @PatchMapping("/verify-email")
    public ResponseEntity<AppResponse<Void>> verifyUserEmail(Authentication authentication,
                                                             @RequestBody VerificationCodeRequest request){

        verifyUserEmailUseCase.execute
                (authentication.getName(), request.verificationCode());

        return ResponseEntity.ok(AppResponse.success(
                "USER_VERIFIED",
                String.format("User with email %s has been verified successfully",
                        authentication.getName())
        ));
    }

}

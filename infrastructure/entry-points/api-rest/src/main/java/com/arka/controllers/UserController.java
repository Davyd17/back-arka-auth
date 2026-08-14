package com.arka.controllers;

import com.arka.dto.output.UserOutput;
import com.arka.mapper.UserRestMapper;
import com.arka.request.VerificationCodeRequest;
import com.arka.response.AppResponse;
import com.arka.usecase.FindCurrentUserByEmailUseCase;
import com.arka.response.UserResponse;
import com.arka.usecase.SendVerificationCodeEmailUseCase;
import com.arka.usecase.VerifyUserEmailUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final FindCurrentUserByEmailUseCase findCurrentUserByEmailUseCase;
    private final UserRestMapper userMapper;
    private final SendVerificationCodeEmailUseCase sendVerificationCodeEmailUseCase;
    private final VerifyUserEmailUseCase verifyUserEmailUseCase;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {

        String email = authentication.getName();
        UserOutput user = findCurrentUserByEmailUseCase.execute(email);

        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PostMapping("/verify-email/request")
    public ResponseEntity<AppResponse<Void>> sendEmailVerificationCode(Authentication authentication) {

        sendVerificationCodeEmailUseCase.execute(authentication.getName());

        return ResponseEntity.ok(AppResponse.success("VERIFICATION_CODE_SENT",
                "Email verification code sent successfully"));
    }

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

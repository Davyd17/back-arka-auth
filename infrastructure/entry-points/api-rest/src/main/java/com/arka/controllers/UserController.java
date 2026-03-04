package com.arka.controllers;

import com.arka.dto.output.UserOutput;
import com.arka.mapper.UserRestMapper;
import com.arka.usecase.FindCurrentUserByEmailUseCase;
import com.arka.usecase.FindSecurityUserByEmailUseCase;
import com.arka.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final FindCurrentUserByEmailUseCase findCurrentUserByEmailUseCase;
    private final UserRestMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {

        String email = authentication.getName();
        UserOutput user = findCurrentUserByEmailUseCase.execute(email);

        return ResponseEntity.ok(userMapper.toResponse(user));
    }
}

package com.arka.controllers;

import com.arka.dto.input.PasswordResetInput;
import com.arka.dto.input.UserLoginInput;
import com.arka.dto.input.UserRegisterInput;
import com.arka.dto.output.AuthLoginOutput;
import com.arka.dto.output.AuthRegisterOutput;
import com.arka.dto.output.UserOutput;
import com.arka.dto.output.UserSummary;
import com.arka.dto.value.TokenDetailsDto;
import com.arka.entities.Role;
import com.arka.enums.RoleName;
import com.arka.mapper.AuthRestMapper;
import com.arka.mapper.UserRestMapper;
import com.arka.request.ForgotPasswordRequest;
import com.arka.request.PasswordResetRequest;
import com.arka.request.UserLoginRequest;
import com.arka.request.UserRegisterRequest;
import com.arka.response.*;
import com.arka.usecase.LoginUserUseCase;
import com.arka.usecase.PasswordResetUseCase;
import com.arka.usecase.RegisterUserUseCase;
import com.arka.usecase.SendPasswordResetTokenEmailUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        })
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private LoginUserUseCase loginUserUseCase;

    @MockitoBean
    private PasswordResetUseCase passwordResetUseCase;

    @MockitoBean
    private SendPasswordResetTokenEmailUseCase sendPasswordResetTokenEmailUseCase;

    @MockitoBean
    private UserRestMapper userMapper;

    @MockitoBean
    private AuthRestMapper authMapper;

    private TokenDetailsDto buildTokenDto() {
        return new TokenDetailsDto(
                "mock-token",
                "Bearer",
                Instant.now().plus(15, ChronoUnit.MINUTES));
    }

    private TokenDetailsResponse buildTokenResponse() {
        return new TokenDetailsResponse(
                "mock-token",
                "Bearer",
                Instant.now().plus(15, ChronoUnit.MINUTES));
    }

    private RoleResponse buildRoleResponse() {
        return new RoleResponse("USER", "User role");
    }

    private Role buildDomainRole() {
        return new Role(1L, RoleName.USER, "User role");
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // given
        UserRegisterRequest request = new UserRegisterRequest(
                "John",
                "jhon",
                "doe",
                "john@arka.com",
                "Password123!");

        UserRegisterInput mappedInput = new UserRegisterInput(
                "John",
                "jhon",
                "doe",
                "john@arka.com",
                "Password123!");

        UserOutput userOutput = new UserOutput(
                1L,
                "John",
                "Doe",
                "john_doe",
                "john@arka.com",
                buildDomainRole(),
                Instant.now());

        AuthRegisterOutput authOutput = new AuthRegisterOutput(buildTokenDto(), userOutput);

        UserResponse userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "john_doe",
                "john@arka.com",
                buildRoleResponse(),
                Instant.now());

        AuthRegisterResponse authResponse = new AuthRegisterResponse(
                buildTokenResponse(), userResponse);

        when(userMapper.toInput(any(UserRegisterRequest.class))).thenReturn(mappedInput);
        when(registerUserUseCase.execute(mappedInput)).thenReturn(authOutput);
        when(authMapper.toResponse(authOutput)).thenReturn(authResponse);

        // when & then
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/auth/register/1"))
                .andExpect(jsonPath("$.tokenDetails.accessToken").value("mock-token"))
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.user.email").value("john@arka.com"));

        verify(registerUserUseCase).execute(mappedInput);
    }


    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        // given
        UserLoginRequest request = new UserLoginRequest("john@arka.com", "Password123!");
        UserLoginInput mappedInput = new UserLoginInput("john@arka.com", "Password123!");

        AuthLoginOutput authOutput = new AuthLoginOutput(
                new UserSummary(1L, "jhon", "john@arka.com"), buildTokenDto());

        AuthLoginResponse authResponse = new AuthLoginResponse(
                new UserSummaryResponse(1L, "jhon", "john@arka.com"), buildTokenResponse());

        when(userMapper.toInput(any(UserLoginRequest.class))).thenReturn(mappedInput);
        when(loginUserUseCase.execute(mappedInput)).thenReturn(authOutput);
        when(authMapper.toResponse(authOutput)).thenReturn(authResponse);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenDetails.accessToken").value("mock-token"));

        verify(loginUserUseCase).execute(mappedInput);
    }

    @Test
    void shouldSendEmailPasswordResetTokenSuccessfully() throws Exception {
        // given
        String email = "john@arka.com";
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        doNothing().when(sendPasswordResetTokenEmailUseCase).execute(email);

        // when & then
        mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PASSWORD_RESET_TOKEN_SENT"))
                .andExpect(jsonPath("$.message").value("Password reset token sent successfully"));

        verify(sendPasswordResetTokenEmailUseCase).execute(email);
    }

    @Test
    void shouldResetPasswordSuccessfully() throws Exception {
        // given
        PasswordResetRequest request = new PasswordResetRequest("NewPassword123!", "valid-reset-token");

        doNothing().when(passwordResetUseCase).execute(any(PasswordResetInput.class));

        // when & then
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PASSWORD_CHANGED"))
                .andExpect(jsonPath("$.message").value("Password has been changed successfully"));

        verify(passwordResetUseCase).execute(new PasswordResetInput("NewPassword123!", "valid-reset-token"));
    }
}

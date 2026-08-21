package com.arka.controllers;

import com.arka.dto.output.UserOutput;
import com.arka.entities.Role;
import com.arka.enums.RoleName;
import com.arka.mapper.UserRestMapper;
import com.arka.request.VerificationCodeRequest;
import com.arka.response.RoleResponse;
import com.arka.response.UserResponse;
import com.arka.usecase.FindCurrentUserByEmailUseCase;
import com.arka.usecase.SendVerificationCodeEmailUseCase;
import com.arka.usecase.VerifyUserEmailUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FindCurrentUserByEmailUseCase findCurrentUserByEmailUseCase;

    @MockitoBean
    private UserRestMapper userMapper;

    @MockitoBean
    private SendVerificationCodeEmailUseCase sendVerificationCodeEmailUseCase;

    @MockitoBean
    private VerifyUserEmailUseCase verifyUserEmailUseCase;

    private Role buildDomainRole() {
        return new Role(1L, RoleName.USER, "User role");
    }

    private RoleResponse buildRoleResponse() {
        return new RoleResponse("USER", "User role");
    }


    @Test
    @WithMockUser(username = "john@arka.com")
    void shouldGetCurrentUserSuccessfully() throws Exception {
        // given
        String email = "john@arka.com";

        UserOutput userOutput = new UserOutput(
                1L,
                "john",
                "doe",
                "John",
                "john@arka.com",
                buildDomainRole(),
                Instant.now());

        UserResponse userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "john_doe",
                "john@arka.com",
                buildRoleResponse(),
                Instant.now());

        when(findCurrentUserByEmailUseCase.execute(email)).thenReturn(userOutput);
        when(userMapper.toResponse(userOutput)).thenReturn(userResponse);

        // when & then
        mockMvc.perform(get("/api/v1/auth/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(email));

        verify(findCurrentUserByEmailUseCase).execute(email);
    }

    @Test
    @WithMockUser(username = "john@arka.com")
    void shouldSendEmailVerificationCodeSuccessfully() throws Exception {
        // given
        String email = "john@arka.com";

        doNothing().when(sendVerificationCodeEmailUseCase).execute(email);

        // when & then
        mockMvc.perform(post("/api/v1/auth/user/verify-email/request")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_CODE_SENT"))
                .andExpect(jsonPath("$.message").value("Email verification code sent successfully"));

        verify(sendVerificationCodeEmailUseCase).execute(email);
    }

    @Test
    @WithMockUser(username = "john@arka.com")
    void shouldVerifyUserEmailSuccessfully() throws Exception {
        // given
        String email = "john@arka.com";
        String code = "123456";
        VerificationCodeRequest request = new VerificationCodeRequest(code);

        doNothing().when(verifyUserEmailUseCase).execute(email, code);

        // when & then
        mockMvc.perform(patch("/api/v1/auth/user/verify-email")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USER_VERIFIED"))
                .andExpect(jsonPath("$.message")
                        .value("User with email " + email + " has been verified successfully"));

        verify(verifyUserEmailUseCase).execute(email, code);
    }
}

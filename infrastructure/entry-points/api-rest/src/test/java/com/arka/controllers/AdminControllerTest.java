package com.arka.controllers;

import com.arka.dto.output.AssignRoleOutput;
import com.arka.enums.RoleName;
import com.arka.service.RoleService;
import com.arka.usecase.AssignRoleUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        })
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AssignRoleUseCase assignRoleUseCase;

    @Test
    void shouldAssignRoleSuccessfully() throws Exception {
        // given
        Long userId = 1L;
        Long roleId = 2L;

        AssignRoleOutput mockOutput = new AssignRoleOutput(
                userId,
                "john.doe@arka.com",
                RoleName.ADMIN,
                "ROLE_ADMIN"
        );

        when(assignRoleUseCase.execute(userId, roleId)).thenReturn(mockOutput);


        // when & then
        mockMvc.perform(patch("/api/v1/admin/users/{userId}/role/{roleId}", userId, roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.roleName").value("ADMIN"));

        verify(assignRoleUseCase).execute(userId, roleId);
    }
}

package com.arka.usecase;

import com.arka.dto.output.AssignRoleOutput;
import com.arka.entities.Role;
import com.arka.entities.User;
import com.arka.enums.RoleName;
import com.arka.exceptions.NotFoundException;
import com.arka.gateway.repository.UserGateway;
import com.arka.service.RoleService;
import com.arka.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignRoleUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AssignRoleUseCase assignRoleUseCase;

    @Test
    void shouldAssignRoleToUserAndReturnOutput() {
        // given
        Role role = new Role(2L, RoleName.ADMIN, "Administrator with full access");

        User user = User.create("johndoe", "john@arka.com", "password123", null);

        when(userService.findById(1L)).thenReturn(user);
        when(roleService.findById(2L)).thenReturn(role);

        // when
        AssignRoleOutput output = assignRoleUseCase.execute(1L, 2L);

        // then
        assertThat(output.roleName()).isEqualTo(RoleName.ADMIN);
        assertThat(output.username()).isEqualTo("johndoe");
        verify(userGateway).save(user);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userService.findById(99L))
                .thenThrow(new NotFoundException("User not found"));

        assertThatThrownBy(() -> assignRoleUseCase.execute(99L, 1L))
                .isInstanceOf(NotFoundException.class);

        verifyNoInteractions(userGateway);
    }

    @Test
    void shouldThrowWhenRoleNotFound() {
        User user = User.create("johndoe", "john@arka.com", "password123", null);
        when(userService.findById(1L)).thenReturn(user);
        when(roleService.findById(99L))
                .thenThrow(new NotFoundException("Role not found"));

        assertThatThrownBy(() -> assignRoleUseCase.execute(1L, 99L))
                .isInstanceOf(NotFoundException.class);

        verifyNoInteractions(userGateway);
    }
}

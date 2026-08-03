package com.arka.controllers;

import com.arka.response.AssignRoleResponse;
import com.arka.usecase.AssignRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AssignRoleUseCase assignRoleUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{userId}/role/{roleId}")
    public ResponseEntity<AssignRoleResponse> assignRole(
            @PathVariable("userId") Long userId,
            @PathVariable("roleId") Long roleId
    ) {

        var output = assignRoleUseCase.execute(userId, roleId);

        return ResponseEntity.ok(AssignRoleResponse.of(output));
    }
}

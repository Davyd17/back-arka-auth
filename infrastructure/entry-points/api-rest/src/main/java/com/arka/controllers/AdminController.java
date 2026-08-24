package com.arka.controllers;

import com.arka.docs.CommonApiResponses;
import com.arka.dto.output.AssignRoleOutput;
import com.arka.response.AssignRoleResponse;
import com.arka.usecase.AssignRoleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management",
        description = "Endpoints for administrative actions and user access control")
public class AdminController {

    private final AssignRoleUseCase assignRoleUseCase;

    @Operation(
            summary = "Assign role to user",
            description = "Updates the assigned role for a given user by ID. \nRequires ADMIN role privileges."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Role assigned successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AssignRoleResponse.class)
            )
    )
    @CommonApiResponses
    @PatchMapping("/users/{userId}/role/{roleId}")
    public ResponseEntity<AssignRoleResponse> assignRole(
            @PathVariable("userId") Long userId,
            @PathVariable("roleId") Long roleId
    ) {

        AssignRoleOutput output = assignRoleUseCase.execute(userId, roleId);

        return ResponseEntity.ok(AssignRoleResponse.of(output));
    }
}

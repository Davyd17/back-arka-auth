package com.arka.controllers;

import com.arka.dto.output.AuthLoginOutput;
import com.arka.dto.output.AuthRegisterOutput;
import com.arka.mapper.AuthRestMapper;
import com.arka.mapper.UserRestMapper;
import com.arka.request.UserLoginRequest;
import com.arka.request.UserRegisterRequest;
import com.arka.response.AuthLoginResponse;
import com.arka.response.AuthRegisterResponse;
import com.arka.usecase.LoginUserUseCase;
import com.arka.usecase.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "auth",description = "Flujo de autenticacion")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    private final UserRestMapper userMapper;
    private final AuthRestMapper authMapper;

    /**
     * Creacion de usuarios en el sistema
     * @param request objeto de entrada para ejecucion del servicio
     * @return AuthResponse con el cliente creado
     */

    @PostMapping("/register")
    @Operation(
            summary = "Registrar un cliente nuevo",
            description = "Registro de clientes ARKA"

    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente creado con exito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRegisterResponse.class)
                    )
            )
    })
    public ResponseEntity<AuthRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {

        // Print the raw DTO to CloudWatch
        System.out.println("DEBUG 1 - DTO: " + request);

        AuthRegisterOutput authOutput =
                registerUserUseCase.execute(userMapper.toInput(request));

        // Print the Entity to see if MapStruct actually moved the data
        System.out.println("DEBUG 2 - Entity User: " + authOutput.user());

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


}

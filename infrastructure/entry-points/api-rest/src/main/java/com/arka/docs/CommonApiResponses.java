package com.arka.docs;


import com.arka.response.AppResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Unauthorized - valid JWT required",
                content = @Content(schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions",
                content = @Content(schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
                content = @Content(schema = @Schema(implementation = AppResponse.class)))
})
public @interface CommonApiResponses {
}

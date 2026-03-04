package com.arka.mapper;

import com.arka.dto.input.UserLoginInput;
import com.arka.dto.input.UserRegisterInput;
import com.arka.dto.output.UserOutput;
import com.arka.dto.output.UserSummary;
import com.arka.model.User;
import com.arka.request.UserLoginRequest;
import com.arka.request.UserRegisterRequest;
import com.arka.response.UserResponse;
import com.arka.response.UserSummaryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    UserRegisterRequest toRequest(UserRegisterInput input);

    UserRegisterInput toInput(UserRegisterRequest request);

    UserLoginInput toInput(UserLoginRequest request);

    UserResponse toResponse(UserOutput output);

    UserSummaryResponse toSummaryResponse(UserSummary summary);
}

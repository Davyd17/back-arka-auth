package com.arka.mapper;

import com.arka.dto.output.UserOutput;
import com.arka.dto.output.UserSummary;
import com.arka.dto.value.SecurityUserDto;
import com.arka.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = new UserMapperImpl();

    UserOutput toOutput(User user);

    UserSummary toSummary(User user);

    @Mapping(target = "emailAsUsername", source = "email")
    SecurityUserDto toDto(User user);
}

package com.arka.user;

import com.arka.dto.value.SecurityUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring")
public interface SecurityUserMapper {

    SecurityUserDto toDto(SecurityUser user);
}

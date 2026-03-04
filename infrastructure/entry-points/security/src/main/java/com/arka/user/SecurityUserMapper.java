package com.arka.user;

import com.arka.dto.value.SecurityUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SecurityUserMapper {

    @Mapping(target = "authorities", ignore = true)
    SecurityUser toSecurityUser(SecurityUserDto dto);

    SecurityUserDto toDto(SecurityUser user);
}

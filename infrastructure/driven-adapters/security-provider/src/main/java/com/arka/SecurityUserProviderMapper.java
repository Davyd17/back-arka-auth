package com.arka;

import com.arka.dto.value.SecurityUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SecurityUserProviderMapper {

    @Mapping(target = "authorities", ignore = true)
    SecurityUser toSecurityUser(SecurityUserDto user);
}

package com.arka.user;

import com.arka.dto.value.SecurityUserDto;
import com.arka.usecase.FindSecurityUserByEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final FindSecurityUserByEmailUseCase findSecurityUserByEmailUseCase;
    private final SecurityUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SecurityUserDto user =
                findSecurityUserByEmailUseCase.execute(username);

        return userMapper.toSecurityUser(user);
    }
}

package com.arka.model;

import com.arka.model.enums.RoleName;
import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Role {

    private Long id;
    private RoleName name;
    private String description;

    public static Role create(RoleName name, String description){
        return Role.builder()
                .name(name)
                .description(description)
                .build();
    }

    public void editDescription(String description){
        this.description = description;
    }
}

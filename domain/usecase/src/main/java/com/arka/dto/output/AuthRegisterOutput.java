package com.arka.dto.output;

import com.arka.dto.value.TokenDetailsDto;

public record AuthRegisterOutput(

        TokenDetailsDto tokenDetails,
        UserOutput user
) {
}

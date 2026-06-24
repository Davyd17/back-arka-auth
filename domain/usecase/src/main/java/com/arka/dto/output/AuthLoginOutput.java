package com.arka.dto.output;

import com.arka.dto.value.TokenDetailsDto;

public record AuthLoginOutput(

        UserSummary user,
        TokenDetailsDto tokenDetails
) {
}

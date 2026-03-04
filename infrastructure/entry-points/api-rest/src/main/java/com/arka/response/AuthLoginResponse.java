package com.arka.response;

public record AuthLoginResponse(

        UserSummaryResponse user,
        TokenDetailsResponse tokenDetails
) {
}

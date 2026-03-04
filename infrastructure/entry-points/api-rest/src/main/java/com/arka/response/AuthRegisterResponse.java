package com.arka.response;

public record AuthRegisterResponse(

        TokenDetailsResponse tokenDetails,
        UserResponse user

) {

}

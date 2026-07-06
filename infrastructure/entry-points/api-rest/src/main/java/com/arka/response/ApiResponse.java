package com.arka.response;

public record ApiResponse<T> (
        String code,
        String message,
        T data
){

    public static <T> ApiResponse<T> success(String code, String message, T data){
        return new ApiResponse<>(code, message, data);
    }

    public static <T> ApiResponse<T> success(String code, String message){
        return new ApiResponse<>(code, message, null);
    }


}

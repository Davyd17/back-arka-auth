package com.arka.response;

public record AppResponse<T> (
        String code,
        String message,
        T data
){

    public static <T> AppResponse<T> success(String code, String message, T data){
        return new AppResponse<>(code, message, data);
    }

    public static <T> AppResponse<T> success(String code, String message){
        return new AppResponse<>(code, message, null);
    }


}

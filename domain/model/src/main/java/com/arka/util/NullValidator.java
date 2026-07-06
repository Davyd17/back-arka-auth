package com.arka.util;

public class NullValidator {

    public static void validate(Object value, String reference){

        if(value == null)
            throw new IllegalArgumentException(
                    String.format("[%s] cannot be null", reference));
    }
}

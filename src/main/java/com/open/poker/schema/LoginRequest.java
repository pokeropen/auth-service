package com.open.poker.schema;

import lombok.NonNull;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class LoginRequest {

    @NonNull
    @With
    @Size(min = 4, max = 40)
    @NotBlank(message = "Please provide a username or an email")
    private final String usernameOrEmail;

    @NonNull
    @With
    @Size(min = 8, max = 20)
    @NotBlank(message = "Please provide a password")
    private final String password;
}

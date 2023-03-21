package com.open.poker.schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NonNull
    @With
    @Size(min = 4, max = 40)
    @NotBlank(message = "Please provide a username or an email")
    private String usernameOrEmail;

    @NonNull
    @With
    @Size(min = 8, max = 20)
    @NotBlank(message = "Please provide a password")
    private String password;
}

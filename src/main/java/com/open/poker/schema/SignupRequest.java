package com.open.poker.schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

@Value()
public class SignupRequest {
    @NonNull @NotBlank(message = "Please provide a username") @Size(min = 4, max = 20) @With private final String username;
    @NonNull @Email @NotBlank(message = "Please provide an email") @Size(min = 1, max = 40) @With private final String email;
    @NonNull @Size(min = 8, max = 20) @NotBlank(message = "Please provide a password")private final String password;
    @With private final String firstName;
    @With private final String lastName;
    @NonNull @Min(18) @Max(120) @With private final int age;
}

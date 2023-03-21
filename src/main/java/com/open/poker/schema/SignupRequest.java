package com.open.poker.schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Data
public class SignupRequest {
    @NonNull @NotBlank(message = "Please provide a username") @Size(min = 4, max = 20) @With private String username;
    @NonNull @Email @NotBlank(message = "Please provide an email") @Size(min = 1, max = 40) @With private String email;
    @NonNull @Size(min = 8, max = 20) @NotBlank(message = "Please provide a password") private String password;
    @With private String firstName;
    @With private String lastName;
    @NonNull @Min(18) @Max(120) @With private int age;
}

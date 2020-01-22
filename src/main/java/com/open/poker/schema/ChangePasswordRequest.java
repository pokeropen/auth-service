package com.open.poker.schema;

import lombok.NonNull;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class ChangePasswordRequest {

    @NonNull
    @With
    @NotBlank(message = "Old Password Can't be empty")
    @Size(min = 8, max = 20)
    private final String oldPassword;
    @NonNull
    @With
    @NotBlank(message = "New Password Can't be empty")
    @Size(min = 8, max = 20)
    private final String newPassword;
    @NonNull
    @With
    @NotBlank(message = "Confirm Password Can't be empty")
    @Size(min = 8, max = 20)
    private final String confirmPassword;
}

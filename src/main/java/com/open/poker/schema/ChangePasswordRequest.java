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
public class ChangePasswordRequest {

    @NonNull
    @With
    @NotBlank(message = "Old Password Can't be empty")
    @Size(min = 8, max = 20)
    private String oldPassword;
    @NonNull
    @With
    @NotBlank(message = "New Password Can't be empty")
    @Size(min = 8, max = 20)
    private String newPassword;
    @NonNull
    @With
    @NotBlank(message = "Confirm Password Can't be empty")
    @Size(min = 8, max = 20)
    private String confirmPassword;
}

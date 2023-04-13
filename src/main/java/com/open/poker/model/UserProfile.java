package com.open.poker.model;

import com.open.poker.schema.SignupRequest;
import com.open.poker.utils.PasswordUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "userprofile", indexes = { @Index(unique = true, name = "username_index", columnList = "username"),
        @Index(unique = true, name = "email_index", columnList = "email")} )
@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public class UserProfile extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @With
    private Long id;
    @NonNull
    @NotBlank
    @Size(min = 4, max = 20)
    @Column(nullable = false)
    @With
    private String username;
    @NonNull
    @NotBlank
    @Size(min = 8, max = 128)
    @Column(nullable = false)
    @With
    private String password;
    @NonNull
    @Email
    @Size(min = 8, max = 40)
    @Column(nullable = false)
    @With
    private String email;
    @NonNull
    @NotBlank
    @Size(min = 1, max = 20)
    @With
    private String firstName;
    @NonNull
    @NotBlank
    @Size(min = 1, max = 20)
    @With
    private String lastName;
    @NonNull
    @Min(18)
    @Max(120)
    @With
    private Integer age;
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isBlocked = false;
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isDeleted = false;

    public static UserProfile of(@NotNull final SignupRequest request) {
        return new UserProfileBuilder().email(request.getEmail()).username(request.getUsername())
                .password(PasswordUtil.hashPwd(request.getPassword())).firstName(Optional.ofNullable(request.getFirstName()).orElse(request.getUsername()))
                .lastName(Optional.ofNullable(request.getLastName()).orElse(request.getUsername())).age(request.getAge()).build();
    }
}

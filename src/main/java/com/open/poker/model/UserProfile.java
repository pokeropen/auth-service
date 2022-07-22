package com.open.poker.model;

import com.open.poker.schema.SignupRequest;
import com.open.poker.utils.PasswordUtil;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Optional;

@Entity
@Table(name = "userprofile", indexes = { @Index(unique = true, name = "username_index", columnList = "username"),
        @Index(unique = true, name = "email_index", columnList = "email")} )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @With
    private Long id;
    @NonNull
    @With
    @NotBlank
    @Size(min = 4, max = 20)
    @Column(nullable = false)
    private String username;
    @NonNull
    @With
    @NotBlank
    @Size(min = 8, max = 128)
    @Column(nullable = false)
    private String password;
    @NonNull
    @With
    @Email
    @Size(min = 8, max = 40)
    @Column(nullable = false)
    private String email;
    @NonNull
    @With
    @NotBlank
    @Size(min = 1, max = 20)
    private String firstName;
    @NonNull
    @With
    @NotBlank
    @Size(min = 1, max = 20)
    private String lastName;
    @NonNull
    @With
    @Min(18)
    @Max(120)
    private Integer age;
    @NonNull
    @With
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isBlocked = false;
    @NonNull
    @With
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isDeleted = false;

    public static UserProfile of(@NotNull final SignupRequest request) {
        return new UserProfileBuilder().email(request.getEmail()).username(request.getUsername())
                .password(PasswordUtil.hashPwd(request.getPassword())).firstName(Optional.ofNullable(request.getFirstName()).orElse(request.getUsername()))
                .lastName(Optional.ofNullable(request.getLastName()).orElse(request.getUsername())).age(request.getAge()).build();
    }
}

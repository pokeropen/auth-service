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
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

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
    private Long id;
    @NonNull
    @NotBlank
    @Size(min = 4, max = 20)
    @Column(nullable = false)
    private String username;
    @NonNull
    @NotBlank
    @Size(min = 8, max = 128)
    @Column(nullable = false)
    private String password;
    @NonNull
    @Email
    @Size(min = 8, max = 40)
    @Column(nullable = false)
    private String email;
    @NonNull
    @NotBlank
    @Size(min = 1, max = 20)
    private String firstName;
    @NonNull
    @NotBlank
    @Size(min = 1, max = 20)
    private String lastName;
    @NonNull
    @Min(18)
    @Max(120)
    private Integer age;
    @NonNull
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isBlocked = false;
    @NonNull
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isDeleted = false;

    public static UserProfile of(@NotNull final SignupRequest request) {
        return new UserProfileBuilder().email(request.getEmail()).username(request.getUsername())
                .password(PasswordUtil.hashPwd(request.getPassword())).firstName(Optional.ofNullable(request.getFirstName()).orElse(request.getUsername()))
                .lastName(Optional.ofNullable(request.getLastName()).orElse(request.getUsername())).age(request.getAge()).build();
    }

    public UserProfile withId(Long id) {
        return this.id == id ? this : new UserProfile(id, this.username, this.password, this.email, this.firstName, this.lastName, this.age, this.isBlocked, this.isDeleted);
    }

    public UserProfile withUsername(@NonNull @NotBlank @Size(min = 4, max = 20) String username) {
        return this.username == username ? this : new UserProfile(this.id, username, this.password, this.email, this.firstName, this.lastName, this.age, this.isBlocked, this.isDeleted);
    }

    public UserProfile withPassword(@NonNull @NotBlank @Size(min = 8, max = 128) String password) {
        return this.password == password ? this : new UserProfile(this.id, this.username, password, this.email, this.firstName, this.lastName, this.age, this.isBlocked, this.isDeleted);
    }

    public UserProfile withEmail(@NonNull @Email @Size(min = 8, max = 40) String email) {
        return this.email == email ? this : new UserProfile(this.id, this.username, this.password, email, this.firstName, this.lastName, this.age, this.isBlocked, this.isDeleted);
    }

    public UserProfile withFirstName(@NonNull @NotBlank @Size(min = 1, max = 20) String firstName) {
        return this.firstName == firstName ? this : new UserProfile(this.id, this.username, this.password, this.email, firstName, this.lastName, this.age, this.isBlocked, this.isDeleted);
    }

    public UserProfile withLastName(@NonNull @NotBlank @Size(min = 1, max = 20) String lastName) {
        return this.lastName == lastName ? this : new UserProfile(this.id, this.username, this.password, this.email, this.firstName, lastName, this.age, this.isBlocked, this.isDeleted);
    }

    public UserProfile withAge(@NonNull @Min(18) @Max(120) Integer age) {
        return this.age == age ? this : new UserProfile(this.id, this.username, this.password, this.email, this.firstName, this.lastName, age, this.isBlocked, this.isDeleted);
    }

    public UserProfile withBlocked(@NonNull boolean isBlocked) {
        return this.isBlocked == isBlocked ? this : new UserProfile(this.id, this.username, this.password, this.email, this.firstName, this.lastName, this.age, isBlocked, this.isDeleted);
    }

    public UserProfile withDeleted(@NonNull boolean isDeleted) {
        return this.isDeleted == isDeleted ? this : new UserProfile(this.id, this.username, this.password, this.email, this.firstName, this.lastName, this.age, this.isBlocked, isDeleted);
    }
}

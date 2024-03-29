package com.open.poker.controller;

import com.open.poker.model.UserProfile;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.schema.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.open.poker.constants.Constants.EMAIL_NOT_AVAILABLE;
import static com.open.poker.constants.Constants.USERNAME_NOT_AVAILABLE;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@Flogger
@RequestMapping(path = "/signup")
@Tag(name = "SignUp Resource", description = "Signing Up User to System")
public class SignUpResource {

    @Autowired
    private UserProfileRepository repository;

    @PostMapping()
    @Operation(summary = "Sign up a new user")
    public ResponseEntity<?> signUpUser(
            @Parameter(description = "Signing-in object of new User to store database", required = true)
            @Valid @RequestBody SignupRequest request) {

        log.atInfo().log("Signing Up new User %s with email %s", request.getUsername(), request.getEmail());

        if (repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(request.getUsername(), request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(USERNAME_NOT_AVAILABLE);
        }

        if (repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(request.getEmail(), request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(EMAIL_NOT_AVAILABLE);
        }

        var res = Try.of(() -> repository.save(UserProfile.of(request)))
                .orElse(Try.failure(new RuntimeException("Unable to save user : " + request.getUsername())));

        if (res.isSuccess()) {
            var location = fromCurrentContextPath().path("/signup/{id}").buildAndExpand(res.get().getId()).toUri();
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res.getCause().getMessage());
        }
    }
}

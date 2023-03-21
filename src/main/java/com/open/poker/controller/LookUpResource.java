package com.open.poker.controller;

import com.open.poker.repository.UserProfileRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Flogger
@RequestMapping(path = "/check")
@Tag(name = "LookUp Resource", description = "Check if User/email exists in system")
public class LookUpResource {

    @Autowired
    private UserProfileRepository repository;

    @GetMapping(path = "/username/{name}")
    @Operation(summary = "Check if user name exists in system or not")
    public ResponseEntity<Boolean> checkUserName(
            @Parameter(description = "username to check", required = true)
            @PathVariable("name") @NotBlank @Size(min = 4, max = 20) String username) {
        log.atInfo().log("Checking if username %s is available", username);
        var response = repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username).isPresent();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/email/{email}")
    @Operation(summary = "Check if email exists in system or not")
    public ResponseEntity<Boolean> checkEmail(
            @Parameter(description = "email to check", required = true)
            @PathVariable("email") @NotBlank @Email @Size(min = 1, max = 40) String email) {
        log.atInfo().log("Checking if email %s is available", email);
        var response = repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(email, email).isPresent();
        return ResponseEntity.ok(response);
    }
}

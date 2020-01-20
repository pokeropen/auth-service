package com.open.poker.controller;

import com.open.poker.repository.UserProfileRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@Flogger
@RequestMapping(path = "/check")
@Api(value = "Check if User/email exists in system")
public class LookUpResource {

    @Autowired
    private UserProfileRepository repository;

    @GetMapping(path = "/username/{name}")
    @ApiOperation(value = "Check if user name exists in system or not", response = String.class)
    public ResponseEntity<Boolean> checkUserName(
            @ApiParam(value = "username to check", required = true)
            @PathVariable("name") @NotBlank @Size(min = 4, max = 20) String username) {
        log.atInfo().log("Checking if username %s is available", username);
        var response = repository.findByUsernameOrEmail(username, username).isPresent();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/email/{email}")
    @ApiOperation(value = "Check if email exists in system or not", response = String.class)
    public ResponseEntity<Boolean> checkEmail(
            @ApiParam(value = "email to check", required = true)
            @PathVariable("email") @NotBlank @Email @Size(min = 1, max = 40) String email) {
        log.atInfo().log("Checking if email %s is available", email);
        var response = repository.findByUsernameOrEmail(email, email).isPresent();
        return ResponseEntity.ok(response);
    }
}

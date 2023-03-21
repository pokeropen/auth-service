package com.open.poker.controller;

import com.open.poker.exception.UserNotFoundException;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.schema.LoginRequest;
import com.open.poker.schema.TokenResponse;
import com.open.poker.utils.JwtUtil;
import com.open.poker.utils.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.open.poker.constants.Constants.INVALID_USER_PWD;

@RestController
@Flogger
@RequestMapping(path = "/login")
@Tag(name = "Logging Resource", description = "Logging User to System")
public class LoginResource {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserProfileRepository repository;

    @PostMapping()
    @Operation(summary = "To log-in user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Login Successfully",
                    content = { @Content(mediaType = "application/json",
                            schema =  @Schema(implementation = TokenResponse.class))})
    })
    public ResponseEntity<?> loginUser(
            @Parameter(description = "User details to log into system", required = true) @Valid @RequestBody LoginRequest request) {

        log.atInfo().log("User %s trying to login", request.getUsernameOrEmail());
        var user = repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(request.getUsernameOrEmail(), request.getUsernameOrEmail()).
                orElseThrow(() -> new UserNotFoundException("User Not Found with name : " + request.getUsernameOrEmail()));

        if (PasswordUtil.matchPwd(request.getPassword(), user.getPassword())) {
            log.atInfo().log("User %s is logged-in", user.getUsername());
            return ResponseEntity.ok(new TokenResponse(jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail())));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_USER_PWD);
        }
    }
}

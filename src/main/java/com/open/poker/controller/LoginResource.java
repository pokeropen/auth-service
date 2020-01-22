package com.open.poker.controller;

import com.open.poker.exception.UserNotFoundException;
import com.open.poker.utils.JwtTokenUtil;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.schema.LoginRequest;
import com.open.poker.schema.TokenResponse;
import com.open.poker.utils.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.open.poker.constants.Constants.INVALID_USER_PWD;

@RestController
@Flogger
@RequestMapping(path = "/login")
@Api(value = "Logging User to System", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginResource {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserProfileRepository repository;

    @PostMapping()
    @ApiOperation(value = "To log-in user", response = String.class)
    public ResponseEntity<?> loginUser(
            @ApiParam(value = "User details to log into system", required = true) @Valid @RequestBody LoginRequest request) {

        log.atInfo().log("User %s trying to login", request.getUsernameOrEmail());
        var user = repository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail()).
                orElseThrow(() -> new UserNotFoundException("User Not Found with name : " + request.getUsernameOrEmail()));

        if (PasswordUtil.matchPwd(request.getPassword(), user.getPassword())) {
            log.atInfo().log("User %s is logged-in", user.getUsername());
            return ResponseEntity.ok(new TokenResponse(jwtTokenUtil.generateToken(user.getId(), user.getUsername(), user.getEmail())));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_USER_PWD);
        }
    }
}

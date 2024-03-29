package com.open.poker.controller;

import com.open.poker.exception.InvalidJwtTokenException;
import com.open.poker.model.UserProfile;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.utils.JwtUtil;
import com.open.poker.utils.ValidationUtil;
import io.vavr.control.Try;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static com.open.poker.constants.TestConstants.CORRUPT_TOKEN;
import static com.open.poker.constants.TestConstants.INVALID_TOKEN_WITH_BEARER;
import static com.open.poker.constants.TestConstants.PASSWORD;
import static com.open.poker.constants.TestConstants.PASSWORD_NEW;
import static com.open.poker.constants.TestConstants.TOKEN;
import static com.open.poker.constants.TestConstants.VALID_TOKEN;
import static com.open.poker.constants.TestConstants.cpr;
import static com.open.poker.constants.TestConstants.user;
import static com.open.poker.utils.PasswordUtil.hashPwd;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ChangePasswordResource.class)
public class ChangePasswordResourceTest {

    @Autowired
    private ChangePasswordResource changePasswordResource;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ValidationUtil validationUtil;

    @MockBean
    private UserProfileRepository repository;


    @Before
    public void setUp() {
        when(jwtUtil.getId(TOKEN)).thenReturn("1");
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(UserProfile.class))).thenReturn(user.withPassword(hashPwd(cpr.getNewPassword())));
        when(validationUtil.isValidToken(VALID_TOKEN)).thenReturn(Try.success(true));
        when(validationUtil.isValidToken(INVALID_TOKEN_WITH_BEARER)).thenReturn(Try.success(false));
        when(validationUtil.isValidToken(CORRUPT_TOKEN)).thenReturn(Try.failure(new InvalidJwtTokenException()));
    }

    @Test
    public void validChangePwdReq() {
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr);
        assertEquals(response.getStatusCode(), OK);
        assertEquals(true, response.getBody());
    }

    @Test
    public void invalidCprWithDiffNewAndConfirmPwd() {
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr.withConfirmPassword(PASSWORD));
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(repository);
        verifyNoInteractions(validationUtil);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidCprWithSameNewAndOldPwd() {
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr.withNewPassword(PASSWORD));
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(repository);
        verifyNoInteractions(validationUtil);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidCprWithInvalidToken() {
        var response = changePasswordResource.changePassword(INVALID_TOKEN_WITH_BEARER, cpr);
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(repository);
        assertEquals(response.getStatusCode(), FORBIDDEN);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidCprWithInvalidTokenException() {
        var response = changePasswordResource.changePassword(CORRUPT_TOKEN, cpr);
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(repository);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidTokenWithWrongId() {
        when(jwtUtil.getId(TOKEN)).thenReturn("2");
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr);
        assertEquals(response.getStatusCode(), NOT_FOUND);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidTokenWithWrongPassword() {
        when(repository.findById(1L)).thenReturn(Optional.of(user.withPassword(hashPwd(PASSWORD_NEW))));
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr);
        assertEquals(response.getStatusCode(), NOT_FOUND);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidWithSaveFailedException() {
        when(repository.save(any(UserProfile.class))).thenThrow(new RuntimeException());
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr);
        assertEquals(response.getStatusCode(), INTERNAL_SERVER_ERROR);
        assertEquals(false, response.getBody());
    }
}

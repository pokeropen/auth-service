package com.open.poker.controller;

import com.open.poker.exception.InvalidJwtTokenException;
import com.open.poker.model.UserProfile;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.utils.JwtTokenUtil;
import com.open.poker.utils.PasswordUtil;
import com.open.poker.utils.ValidationUtil;
import io.vavr.control.Try;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static com.open.poker.constants.TestConstants.*;
import static com.open.poker.utils.PasswordUtil.hashPwd;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ChangePasswordResource.class)
public class ChangePasswordResourceTest {

    @Autowired
    private ChangePasswordResource changePasswordResource;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private ValidationUtil validationUtil;

    @MockBean
    private UserProfileRepository repository;


    @Before
    public void setUp() {
        when(jwtTokenUtil.getId(TOKEN)).thenReturn("1");
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(UserProfile.class))).thenReturn(user.withPassword(hashPwd(cpr.getNewPassword())));
        when(validationUtil.isValidToken(VALID_TOKEN)).thenReturn(Try.success(true));
        when(validationUtil.isValidToken(INVALID_TOKEN_WITH_BEARER)).thenReturn(Try.success(false));
        when(validationUtil.isValidToken(CORRUPT_TOKEN)).thenReturn(Try.failure(new InvalidJwtTokenException()));
    }

    @Test
    public void validChangePwdReq() {
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(true, response.getBody());
    }

    @Test
    public void invalidCprWithDiffNewAndConfirmPwd() {
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr.withConfirmPassword(PASSWORD));
        verify(jwtTokenUtil, times(0)).validateToken(anyString());
        verifyNoInteractions(jwtTokenUtil);
        verifyNoInteractions(repository);
        verifyNoInteractions(validationUtil);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidCprWithSameNewAndOldPwd() {
        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr.withNewPassword(PASSWORD));
        verify(jwtTokenUtil, times(0)).validateToken(anyString());
        verifyNoInteractions(jwtTokenUtil);
        verifyNoInteractions(repository);
        verifyNoInteractions(validationUtil);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(false, response.getBody());
    }

//    @Test
//    public void invalidCprWithInvalidToken() {
//        var response = changePasswordResource.changePassword(VALID_TOKEN, cpr);
//        verify(jwtTokenUtil, times(0)).validateToken(anyString());
//        verifyNoInteractions(jwtTokenUtil);
//        verifyNoInteractions(repository);
//        verifyNoInteractions(validationUtil);
//        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
//        assertEquals(false, response.getBody());
//    }
//
//    @Test
//    public void invalidEmptyToken() {
//        var response = changePasswordResource.authorize(null);
//        verify(jwtTokenUtil, times(0)).validateToken(anyString());
//        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
//        assertEquals(false, response.getBody());
//    }
//
//    @Test
//    public void invalidTokenParsingError() {
//        when(jwtTokenUtil.validateToken(anyString())).thenThrow(new InvalidJwtTokenException());
//        var response = changePasswordResource.authorize(INVALID_TOKEN_WITH_BEARER);
//        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
//        assertEquals(false, response.getBody());
//    }
//
//    @Test
//    public void invalidTokenExpired() {
//        when(jwtTokenUtil.validateToken(anyString())).thenReturn(false);
//        var response = changePasswordResource.authorize(INVALID_TOKEN_WITH_BEARER);
//        verify(jwtTokenUtil, times(1)).validateToken(anyString());
//        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
//        assertEquals(false, response.getBody());
//    }
}

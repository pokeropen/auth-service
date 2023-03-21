package com.open.poker.controller;

import com.open.poker.model.UserProfile;
import com.open.poker.repository.UserProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Objects;
import java.util.Optional;

import static com.open.poker.constants.Constants.EMAIL_NOT_AVAILABLE;
import static com.open.poker.constants.Constants.USERNAME_NOT_AVAILABLE;
import static com.open.poker.constants.TestConstants.EMAIL;
import static com.open.poker.constants.TestConstants.EMAIL_NEW;
import static com.open.poker.constants.TestConstants.USERNAME;
import static com.open.poker.constants.TestConstants.USERNAME_NEW;
import static com.open.poker.constants.TestConstants.signUpReq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SignUpResource.class)
public class SignUpResourceTest {

    @Autowired
    private SignUpResource signUpResource;

    @MockBean
    private UserProfileRepository userProfileRepository;

    final UserProfile user = UserProfile.of(signUpReq);

    @Before
    public void setUp() {
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(UserProfile.of(signUpReq).withId(1L));
    }

    @Test
    public void validateSignupPositive() {
        var response = signUpResource.signUpUser(signUpReq);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(Objects.requireNonNull(response.getHeaders().getLocation()).toString().endsWith("1"));
    }

    @Test
    public void validateSignupFailUsername() {
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(EMAIL, EMAIL)).thenReturn(Optional.of(user));
        var response = signUpResource.signUpUser(signUpReq.withEmail(EMAIL_NEW));
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(), USERNAME_NOT_AVAILABLE);
    }

    @Test
    public void validateSignupFailEmail() {
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(EMAIL, EMAIL)).thenReturn(Optional.of(user));
        var response = signUpResource.signUpUser(signUpReq.withUsername(USERNAME_NEW));
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(), EMAIL_NOT_AVAILABLE);
    }

    @Test
    public void validateSignupFailException() {
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(EMAIL, EMAIL)).thenReturn(Optional.of(user));
        when(userProfileRepository.save(any(UserProfile.class))).thenThrow(RuntimeException.class);
        var response = signUpResource.signUpUser(signUpReq.withUsername(USERNAME_NEW).withEmail(EMAIL_NEW));
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertTrue(Objects.requireNonNull(response.getBody()).toString().contains(USERNAME_NEW));
    }

    @Test
    public void validateSignupPositiveNewUsernameEmail() {
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(EMAIL, EMAIL)).thenReturn(Optional.of(user));
        var response = signUpResource.signUpUser(signUpReq.withUsername(USERNAME_NEW).withEmail(EMAIL_NEW));
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(Objects.requireNonNull(response.getHeaders().getLocation()).toString().endsWith("1"));
    }
}

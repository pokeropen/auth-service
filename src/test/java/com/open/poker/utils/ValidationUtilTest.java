package com.open.poker.utils;

import com.open.poker.exception.InvalidJwtTokenException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.open.poker.constants.TestConstants.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ValidationUtil.class)
public class ValidationUtilTest {

    @Autowired
    private ValidationUtil validationUtil;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Before
    public void setUp() {
        when(jwtTokenUtil.validateToken(TOKEN)).thenReturn(true);
    }

    @Test
    public void testValidToken() {
        var response = validationUtil.isValidToken(VALID_TOKEN);
        assertTrue(response.isSuccess());
        assertTrue(response.get());
    }

    @Test
    public void testNullToken() {
        var response = validationUtil.isValidToken(null);
        assertTrue(response.isSuccess());
        assertFalse(response.get());
        verifyNoInteractions(jwtTokenUtil);
    }

    @Test
    public void testEmptyToken() {
        var response = validationUtil.isValidToken("");
        assertTrue(response.isSuccess());
        assertFalse(response.get());
        verifyNoInteractions(jwtTokenUtil);
    }

    @Test
    public void testInvalidTokenWithoutBearer() {
        var response = validationUtil.isValidToken(INVALID_TOKEN_WITHOUT_BEARER);
        assertTrue(response.isSuccess());
        assertFalse(response.get());
        verifyNoInteractions(jwtTokenUtil);
    }

    @Test
    public void testInvalidTokenWithBearer() {
        var response = validationUtil.isValidToken(INVALID_TOKEN_WITH_BEARER);
        assertTrue(response.isSuccess());
        assertFalse(response.get());
    }

    @Test
    public void testInvalidTokenWithException() {
        when(jwtTokenUtil.validateToken(anyString())).thenThrow(new InvalidJwtTokenException());
        var response = validationUtil.isValidToken(INVALID_TOKEN_WITH_BEARER);
        assertTrue(response.isFailure());
    }
}

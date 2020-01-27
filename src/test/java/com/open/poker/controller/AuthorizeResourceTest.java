package com.open.poker.controller;

import com.open.poker.exception.InvalidJwtTokenException;
import com.open.poker.utils.JwtUtil;
import com.open.poker.utils.ValidationUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.open.poker.constants.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AuthorizeResource.class, ValidationUtil.class})
public class AuthorizeResourceTest {

    @Autowired
    private AuthorizeResource authorizeResource;

    @MockBean
    private JwtUtil jwtUtil;


    @Before
    public void setUp() {
        when(jwtUtil.validateToken(TOKEN)).thenReturn(true);
    }

    @Test
    public void validToken() {
        var response = authorizeResource.authorize(VALID_TOKEN);
        assertEquals(response.getStatusCode(), OK);
        assertEquals(true, response.getBody());
    }

    @Test
    public void invalidTokenWithoutBearer() {
        var response = authorizeResource.authorize(INVALID_TOKEN_WITHOUT_BEARER);
        verify(jwtUtil, times(0)).validateToken(anyString());
        assertEquals(response.getStatusCode(), FORBIDDEN);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidEmptyToken() {
        var response = authorizeResource.authorize(null);
        verify(jwtUtil, times(0)).validateToken(anyString());
        assertEquals(response.getStatusCode(), FORBIDDEN);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidTokenParsingError() {
        when(jwtUtil.validateToken(anyString())).thenThrow(new InvalidJwtTokenException());
        var response = authorizeResource.authorize(INVALID_TOKEN_WITH_BEARER);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
        assertEquals(false, response.getBody());
    }

    @Test
    public void invalidTokenExpired() {
        when(jwtUtil.validateToken(anyString())).thenReturn(false);
        var response = authorizeResource.authorize(INVALID_TOKEN_WITH_BEARER);
        verify(jwtUtil, times(1)).validateToken(anyString());
        assertEquals(response.getStatusCode(), FORBIDDEN);
        assertEquals(false, response.getBody());
    }
}

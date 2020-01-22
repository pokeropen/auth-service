package com.open.poker.controller;

import com.open.poker.utils.JwtTokenUtil;
import com.open.poker.utils.ValidationUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.open.poker.constants.TestConstants.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AuthorizeResource.class, ValidationUtil.class})
public class AuthorizeResourceTest {

    @Autowired
    private AuthorizeResource authorizeResource;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;


    @Before
    public void setUp() {
        Mockito.when(jwtTokenUtil.validateToken(VALID_TOKEN.substring(7))).thenReturn(true);
    }

    @Test
    public void validToken() {
        var response = authorizeResource.authorize(VALID_TOKEN);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(true, response.getBody());
    }

    @Test
    public void invalidTokenWithoutBearer() {
        var response = authorizeResource.authorize(INVALID_TOKEN_WITHOUT_BEARER);
        Mockito.verify(jwtTokenUtil, Mockito.times(0)).validateToken(Mockito.anyString());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(false, response.getBody());
    }

    @Test
    public void invalidEmptyToken() {
        var response = authorizeResource.authorize(null);
        Mockito.verify(jwtTokenUtil, Mockito.times(0)).validateToken(Mockito.anyString());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(false, response.getBody());
    }

    @Test
    public void invalidTokenParsingError() {
        Mockito.when(jwtTokenUtil.validateToken(Mockito.anyString())).thenThrow(new RuntimeException());
        var response = authorizeResource.authorize(INVALID_TOKEN_WITH_BEARER);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertEquals(false, response.getBody());
    }

    @Test
    public void invalidTokenExpired() {
        Mockito.when(jwtTokenUtil.validateToken(Mockito.anyString())).thenReturn(false);
        var response = authorizeResource.authorize(INVALID_TOKEN_WITH_BEARER);
        Mockito.verify(jwtTokenUtil, Mockito.times(1)).validateToken(Mockito.anyString());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(false, response.getBody());
    }
}

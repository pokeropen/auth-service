package com.open.poker.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static com.open.poker.constants.TestConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class PasswordUtilTest {

    @Test
    public void testHashPwd() {
        var hashPwd = PasswordUtil.hashPwd(PASSWORD);
        assertNotEquals(PASSWORD, hashPwd);
    }

    @Test
    public void testMatchPwd() {
        var hashPwd = PasswordUtil.hashPwd(PASSWORD);
        assertTrue( PasswordUtil.matchPwd(PASSWORD, hashPwd));
        assertFalse(PasswordUtil.matchPwd(PASSWORD_NEW, hashPwd));
    }

    @Test
    public void testValidChangePasswordReq() {
        assertTrue(PasswordUtil.isValidChangePwdReq(cpr));
        assertFalse(PasswordUtil.isValidChangePwdReq(cpr.withConfirmPassword(PASSWORD)));
        assertFalse(PasswordUtil.isValidChangePwdReq(cpr.withNewPassword(PASSWORD).withConfirmPassword(PASSWORD)));
    }
}

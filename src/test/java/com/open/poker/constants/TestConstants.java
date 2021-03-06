package com.open.poker.constants;

import com.open.poker.model.UserProfile;
import com.open.poker.schema.ChangePasswordRequest;
import com.open.poker.schema.SignupRequest;

import static com.open.poker.utils.PasswordUtil.hashPwd;

public final class TestConstants {
    public static final String USERNAME = "daim";
    public static final String USERNAME_NEW = "daim_1";
    public static final String EMAIL = "rishabhdaim1991@gmail.com";
    public static final String EMAIL_NEW = "rishabhdaim1991@yahoo.com";
    public static final String PASSWORD = "daim1234";
    public static final String PASSWORD_NEW = "daim123456";
    public static final String FIRST_NAME = "daim";
    public static final String LAST_NAME = "daim";
    public static final String TOKEN = "TOKEN";
    public static final String VALID_TOKEN = "Bearer TOKEN";
    public static final String CORRUPT_TOKEN = "Bearer TOKEN_CORRUPT";
    public static final String INVALID_TOKEN_WITHOUT_BEARER = "TOKEN_INVALID";
    public static final String INVALID_TOKEN_WITH_BEARER = "Bearer TOKEN_INVALID";
    public static final int MIN_VALID_AGE = 18;
    public static final int MAX_VALID_AGE = 120;

    public static final UserProfile user = UserProfile.builder().username(USERNAME).email(EMAIL).password(hashPwd(PASSWORD))
            .firstName(FIRST_NAME).lastName(LAST_NAME).id(1L).age(MIN_VALID_AGE).build();

    public static final ChangePasswordRequest cpr = new ChangePasswordRequest(PASSWORD, PASSWORD_NEW, PASSWORD_NEW);

    public static final SignupRequest signUpReq = new SignupRequest(USERNAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, 18);

    private TestConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

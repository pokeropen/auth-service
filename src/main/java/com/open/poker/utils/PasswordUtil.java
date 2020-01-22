package com.open.poker.utils;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private PasswordUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String hashPwd(final String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt(10));
    }

    public static boolean matchPwd(final String pwd, final String hashPwd) {
        return BCrypt.checkpw(pwd, hashPwd);
    }
}

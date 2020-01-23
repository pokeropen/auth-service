package com.open.poker.utils;

import com.open.poker.schema.ChangePasswordRequest;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

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

    public static boolean isValidChangePwdReq(final ChangePasswordRequest request) {
        return Objects.equals(request.getNewPassword(), request.getConfirmPassword()) && !Objects.equals(request.getNewPassword(), request.getOldPassword());
    }
}

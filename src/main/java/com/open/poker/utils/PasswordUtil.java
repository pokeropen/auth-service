package com.open.poker.utils;

import lombok.experimental.UtilityClass;
import org.mindrot.jbcrypt.BCrypt;

@UtilityClass
public class PasswordUtil {

    public String hashPwd(final String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt(10));
    }

    public boolean matchPwd(final String pwd, final String hashPwd) {
        return BCrypt.checkpw(pwd, hashPwd);
    }
}

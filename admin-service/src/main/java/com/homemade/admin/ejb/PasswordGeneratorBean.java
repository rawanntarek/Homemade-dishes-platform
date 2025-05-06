package com.homemade.admin.ejb;

import javax.ejb.Stateless;
import java.security.SecureRandom;

@Stateless
public class PasswordGeneratorBean {

    private static final String CHARACTERStouse = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
    private final SecureRandom random = new SecureRandom();

    public String generatePassword(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERStouse.length());
            builder.append(CHARACTERStouse.charAt(index));
        }
        return builder.toString();
    }
}

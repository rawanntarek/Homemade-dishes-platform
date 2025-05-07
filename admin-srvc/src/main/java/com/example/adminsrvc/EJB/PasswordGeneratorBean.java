package com.example.adminsrvc.EJB;

import jakarta.ejb.Stateless;

import java.security.SecureRandom;

@Stateless
public class PasswordGeneratorBean {

    private static final String CHARACTERStouse = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
    private final SecureRandom random = new SecureRandom();

    public String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERStouse.length());
            password.append(CHARACTERStouse.charAt(index));
        }
        return password.toString();
    }
}

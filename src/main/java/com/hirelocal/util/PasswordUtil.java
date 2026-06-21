package com.hirelocal.util;

import java.security.MessageDigest;

public class PasswordUtil {

    public static String encode(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(plain.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return plain;
        }
    }

    public static boolean matches(String plain, String encoded) {
        return encode(plain).equals(encoded);
    }
}
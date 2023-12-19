package com.threlease.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class StringUtil {
    // SHA256을 적용하는 메소드
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // 적용하고 배열로 넘김
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            // 해시를 16진수로 변환
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}

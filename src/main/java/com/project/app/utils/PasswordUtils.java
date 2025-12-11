package main.java.com.project.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
    // Hash password + salt pake SHA-256
    public static String hashWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passWithSalt = password + salt;
            byte[] hash = md.digest(passWithSalt.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generate salt random
    public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16]; // 16 bytes = 128 bit
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Cek password input sama hash
    public static boolean checkPassword(String inputPassword, String storedHash, String salt) {
        String hashInput = hashWithSalt(inputPassword, salt);
        return hashInput.equals(storedHash);
    }
}

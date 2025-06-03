package de.hitec.nhplus.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for hashing passwords using SHA-256.
 * <p>
 * Note: For production use, it's recommended to use a stronger password hashing function
 * like bcrypt, scrypt, or Argon2 which are specifically designed for password security.
 * SHA-256 is fast and therefore more vulnerable to brute-force attacks.
 */

public class PasswordUtil {

    /**
     * Hashes a given password using SHA-256.
     *
     * @param password the plain text password to hash
     * @return the SHA-256 hash of the password in hexadecimal format
     * @throws RuntimeException if SHA-256 algorithm is not supported on the platform
     */

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b)); // Hex
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }
}

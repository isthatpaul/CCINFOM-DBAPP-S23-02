package Services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Utility class for hashing and verifying passwords.
 * Uses PBKDF2 with SHA-256 to safely store credentials.
 */
public final class PasswordUtils {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32; // 256 bits
    private static final int ITERATIONS = 65536;

    private PasswordUtils() {
    }

    /**
     * Hash a plain text password using PBKDF2. The stored format is salt:hash.
     */
    public static String hashPassword(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        byte[] hash = pbkdf2(password.toCharArray(), salt);
        return Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies the provided password against the stored hash.
     * Supports legacy plain-text values (without a salt) to maintain compatibility.
     */
    public static boolean verifyPassword(String password, String storedValue) {
        if (password == null || storedValue == null || storedValue.isEmpty()) {
            return false;
        }

        if (!storedValue.contains(":")) {
            // Legacy record that still stores plain text
            return password.equals(storedValue);
        }

        String[] parts = storedValue.split(":");
        if (parts.length != 2) {
            return false;
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
        byte[] actualHash = pbkdf2(password.toCharArray(), salt);

        return slowEquals(expectedHash, actualHash);
    }

    /**
     * Indicates whether a stored password should be upgraded to the secure format.
     */
    public static boolean needsRehash(String storedValue) {
        return storedValue != null && !storedValue.contains(":");
    }

    private static byte[] pbkdf2(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_LENGTH * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Failed to hash password", e);
        }
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) {
            return false;
        }

        int diff = 0;
        for (int i = 0; i < a.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}


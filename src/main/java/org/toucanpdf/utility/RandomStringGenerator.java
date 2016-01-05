package org.toucanpdf.utility;

import java.util.Random;

/**
 * Generates a random string.
 * @author Dylan de Wolff
 *
 */
public final class RandomStringGenerator {
    /**
     * A string containing all letters of the alphabet. This can be used to generate a random string.
     */
    public static final String DEFAULT_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    /**
     * A string containing all capitalized letters of the alphabet. This can be used to generate a random string.
     */
    public static final String DEFAULT_CAPS_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private RandomStringGenerator() {

    }

    /**
     * Generates a random string of the given length.
     * @param length Length of the string to generate.
     * @return Generated string.
     */
    public static String generateRandomString(int length) {
        return generateRandomString(DEFAULT_CHARACTERS, length);
    }

    /**
     * Generates a random string out of the given possible characters and of the given length.
     * @param possibleCharacters String containing the characters to use.
     * @param length Length of the string to generate.
     * @return Generated string.
     */
    public static String generateRandomString(String possibleCharacters, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(possibleCharacters.charAt(random.nextInt(possibleCharacters.length())));
        }
        return sb.toString();
    }
}

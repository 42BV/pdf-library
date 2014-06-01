package nl.mad.toucanpdf.utility;

import java.util.Random;

public final class RandomStringGenerator {
	public static final String DEFAULT_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
	public static final String DEFAULT_CAPS_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private RandomStringGenerator() {
		
	}
	
	public static String generateRandomString(int length) {
		return generateRandomString(DEFAULT_CHARACTERS, length);
	}
	
	public static String generateRandomString(String possibleCharacters, int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; ++i) {
			sb.append(possibleCharacters.charAt(random.nextInt(possibleCharacters.length())));
		}
		return sb.toString();		
	}
}

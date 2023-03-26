package Helpers;

import java.util.Random;


/**
 * StringUtil class provides the list customized String functions.
 */
public class StringUtils {
	
	private StringUtils() {
		
	}

	public static final String ALLOWED_CHARS = " _^$%+ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.(:)0123456789-#,\\/''&@`?*=!;\012\015";
	
   

	/**
	 * Method to check if string char are upper case
	 * @param str String
	 * @return boolean true/false
	 */
	public static boolean isAllUpperCase(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isLetter(c) && Character.isLowerCase(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true of the given string is null or zero-length without trimming.
	 * @param str String
	 * @return Returns true of the given string is null or zero-length without trimming.
	 */
	public static boolean isEmptyString(String str) {
		return isEmptyString(str, false);
	}

    /**
     * @param str string input
     * @param trim, boolean value to determine if the string s is
     * to be trimmed before checking it's length.
     * @return Returns true of the given string is null or zero-length.
     */
    public static boolean isEmptyString(String str, boolean trim) {
        return str == null || (trim ? str.trim().length() == 0 : str.length() == 0);
    }

    /**
     * Method to generate Random
     */
	private static final Random RANDOM = new Random();

	/**
	 * @param minChars min characters
	 * @param maxChars max characters
	 * @return Return a random alphabetic string of at least minChars and at most maxChars in length.
	 */
	public static String randomAlphabetic(int minChars, int maxChars) {
		StringBuilder buffer = new StringBuilder();
		while (buffer.length() < minChars) {
			long l = RANDOM.nextLong();
			String base36 = Long.toString(l, 36);
			for (int i = 0; i < base36.length() && buffer.length() < maxChars; i++) {
				char c = base36.charAt(i);
				if (Character.isLetter(c)) {
					buffer.append(c);
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * @param chars alphabets size
	 * @return random alphabets for specified size
	 */
	public static String randomAlphabetic(int chars) {
		return randomAlphabetic(chars, chars);
	}

	/**
	 * @return random alphabets till max integer size
	 */
	public static String randomAlphabetic() {
		return randomAlphabetic(1, Integer.MAX_VALUE);
	}

	/**
	 * Method to return true if String is not empty and is equal to Yes/true
	 * @param s input string
	 * @return true/false
	 */
	public static boolean toBoolean(String s) {
		return ! isEmptyString(s) && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes"));
	}

	/**
	 * @param c special char
	 * @param specialChar special char array
	 * @return boolean true/false if special char present
	 */
    public static boolean isSpecialCharacter(char c, char[] specialChar) {
        for (char ch : specialChar) {
            if (c == ch) {
                return true;
            }  
        }
        return false;
    }

    /**
     * This method checks for Spaces in the string and returns true if it finds one
     * @param s input string 
     * @return true if space is present
     */
    public static boolean containsSpaceChars(String s) {

        char[] ch = s.toCharArray();
        for(int i=0;i<ch.length;i++){
            if(Character.isWhitespace(ch[i]))
                return true;
        }
        return false;
    }

} //End of StringUtil class
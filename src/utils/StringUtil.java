package utils;

public class StringUtil {
    public static String removeSpecialCharAndWhiteSpace(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }
}

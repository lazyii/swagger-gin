package org.rainday.logging;

public class Utils {
    
    public static String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static final boolean isWindows;
    
    static {
        String os = System.getProperty("os.name").toLowerCase();
        isWindows = os.contains("win");
    }
    
    public static boolean isWindows() {
        return isWindows;
    }
    
}

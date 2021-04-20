package utility;

import java.util.HashMap;
import java.util.Scanner;

// TODO : kamyar
public class Utility {

    private static Scanner scanner = new Scanner(System.in);

    public static HashMap<String, String> getCommand(String command) {
        HashMap<String, String> map = new HashMap<String, String>();

        return map;
    }

    public static String getNextLine() {
        return scanner.nextLine();
    }

    public static boolean isCommandValid(HashMap<String, String> map, String[] mustAttributes , String[] optionalAttributes) {

        return false;
    }

    public static int rollADice() {

        return 0;
    }

    public static boolean tossACoin() {

        return false;
    }
}

package utility;

import java.util.HashMap;
import java.util.Scanner;

public class Utility {

    private static Scanner scanner = new Scanner(System.in);

    public HashMap<String, String> getCommand(String command) {
        HashMap<String, String> map = new HashMap<String, String>();

        return map;
    }

    public static String getNextLine() {
        return scanner.nextLine();
    }

    public boolean isCommandValid(HashMap<String, String> map, String[] attributes) {

        return false;
    }

    public int rollADice() {

        return 0;
    }

    public boolean tossACoin() {

        return false;
    }
}

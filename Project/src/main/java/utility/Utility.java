package utility;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO : kamyar
public class Utility {

    private static Scanner scanner = new Scanner(System.in);

    public static HashMap<String, String> getCommand(String command) {
        HashMap<String, String> map = new HashMap<String, String>();
        String regex = "--(\\w+)\\s(\\w+)";
        Pattern attributePattern = Pattern.compile(regex);
        Matcher matcher = attributePattern.matcher(command);
        while (matcher.find()) {
            String attribute = matcher.group(1);
            String value = matcher.group(2);
            if (map.containsKey(attribute)) return null;
            map.put(attribute, value);
        }
        return map;
    }

    public static String getNextLine() {
        return scanner.nextLine();
    }

    public static boolean isCommandValid(HashMap<String, String> map, String[] mustAttributes, String[] optionalAttributes) {
        int mapSize = map.size();
        for(String i:mustAttributes){
            if(!map.containsKey(i)) return false;
        }
        mapSize -= mustAttributes.length;
        if(optionalAttributes != null) {
            for (String i : optionalAttributes) {
                if (map.containsKey(i)) mapSize--;
            }
        }
        if(mapSize == 0) return true;
        return false;
    }

    public static int rollADice() {
        Random rand = new Random();
        return rand.nextInt(6) + 1;
    }

    public static boolean tossACoin() {
        Random rand = new Random();
        return rand.nextBoolean();
    }
}

package utility;

import data.Printer;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO : kamyar
public class Utility {

    private static final Scanner scanner = new Scanner(System.in);

    // returns null if any command comes multiple times to indicate invalidity
    // if an attribute does not have any arguments, it'll be mapped to null.
    public static HashMap<String, String> getCommand(String command) {
        HashMap<String, String> map = new HashMap<>();
        String regex = "--(\\w+)\\s(\\w+)";
        Matcher matcher = getCommandMatcher(command, regex);
        while (matcher.find()) {
            String attribute = matcher.group(1);
            String value = matcher.group(2);
            if (map.containsKey(attribute)) return null;
            map.put(attribute, value);
        }
        command = command.replaceAll("--(\\w+)\\s(\\w+)", "");
        matcher = getCommandMatcher(command, "--(\\w+)");
        while (matcher.find()) {
            String attribute = matcher.group(1);
            if (map.containsKey(attribute)) return null;
            map.put(attribute, null);
        }
        return map;
    }

    public static String getNextLine() {
        return scanner.nextLine();
    }


    // Be Aware: if the command has attributes without an argument always check them with areAttributesValid function
    public static boolean isCommandValid(HashMap<String, String> map, String[] mustAttributes, String[] optionalAttributes) {
        if (map == null) {
            return mustAttributes.length == 0;
        }
        int mapSize = map.size();
        if (mustAttributes != null) {
            for (String i : mustAttributes) {
                if (!map.containsKey(i)) return false;
            }
        }
        mapSize -= mustAttributes.length;
        if (optionalAttributes != null) {
            for (String i : optionalAttributes) {
                if (map.containsKey(i)) mapSize--;
            }
        }
        return (mapSize == 0);
    }
    public static boolean areAttributesValid(HashMap<String, String> map, String[] attributesWithArgument, String[] attributesWithoutAnArgument) {
        if (attributesWithArgument != null) {
            for (String i : attributesWithArgument) {
                if (!map.containsKey(i)
                        || map.get(i) == null) return false;
            }
        }
        if (attributesWithoutAnArgument != null) {
            for (String i : attributesWithoutAnArgument) {
                if (!map.containsKey(i)
                        || map.get(i) != null) return false;
            }
        }
        return true;
    }

    public static int rollADice() {
        Random rand = new Random();
        return rand.nextInt(6) + 1;
    }

    public static boolean tossACoin() {
        Random rand = new Random();
        return rand.nextBoolean();
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        return Pattern.compile(regex).matcher(input);
    }

    @SneakyThrows
    public static ArrayList<String[]> getArrayListFromCSV(String fileLocation) {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));
        ArrayList<String[]> result = new ArrayList<>();
        String newLine = "";
        while ((newLine = bufferedReader.readLine()) != null)
            result.add(trimAll(newLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")));
        return result;
    }

    public static String removeMultipleSpaces(String input) {
        input = input.replaceAll("\\s+", " ");
        return input;
    }

    public static String[] trimAll(String[] array) {
        for (int i = 0; i < array.length; ++i)
            array[i] = array[i].trim();
        return array;
    }

    public static boolean checkAndPrompt(boolean condition, String promptMessage) {
        if (condition) Printer.prompt(promptMessage);
        return condition;
    }
}

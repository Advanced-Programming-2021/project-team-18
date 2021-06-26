package utility;

import data.Printer;
import game.AIPlayer;
import game.Player;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO : kamyar
public class Utility {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    // returns null if any command comes multiple times to indicate invalidity
    // if an attribute does not have any arguments, it'll be mapped to null.
    public static HashMap<String, String> getCommand(String command) {
        HashMap<String, String> map = new HashMap<>();
        String regex = "--([\\w|-]+)\\s+([^-]+)";
        Matcher matcher = getCommandMatcher(command, regex);
        while (matcher.find()) {
            String attribute = matcher.group(1);
            String value = matcher.group(2).trim();
            if (map.containsKey(attribute)) return null;
            map.put(attribute, value);
        }
        command = command.replaceAll("--([\\w|-]+)\\s+([^-]+)", "");
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
        if (mustAttributes != null)
            mapSize -= mustAttributes.length;
        if (optionalAttributes != null) {
            for (String i : optionalAttributes) {
                if (map.containsKey(i)) mapSize--;
            }
        }
        return (mapSize == 0);
    }

    public static boolean areAttributesInvalid(HashMap<String, String> map, String[] attributesWithArgument, String[] attributesWithoutAnArgument) {
        if (attributesWithArgument != null) {
            for (String i : attributesWithArgument) {
                if (!map.containsKey(i)
                        || map.get(i) == null) return true;
            }
        }
        if (attributesWithoutAnArgument != null) {
            for (String i : attributesWithoutAnArgument) {
                if (!map.containsKey(i)
                        || map.get(i) != null) return true;
            }
        }
        return false;
    }

    public static int getARandomNumber(int bound){
        if (bound == 0) return -1;
        if (random.nextBoolean()) random.setSeed(LocalDateTime.now().getNano());
        return random.nextInt(bound);
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        return Pattern.compile(regex).matcher(input);
    }

    @SneakyThrows
    public static ArrayList<String[]> getArrayListFromCSV(String fileLocation) {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));
        ArrayList<String[]> result = new ArrayList<>();
        String newLine;
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

    public static String askPlayer(Player player, String message, ArrayList<String> options) {
        if (player instanceof AIPlayer || player == null)
            return options.get(getARandomNumber(options.size()));
        Printer.prompt(player.getUser().getNickname() + ": " + message);
        System.out.print("your options are (");
        for (String option : options)
            System.out.print(option + " ");
        System.out.println(")");
        while (true) {
            String response = getNextLine();
            boolean exists = false;
            for (String option : options)
                if (option.equals(response)) {
                    Printer.prompt("successful response");
                    return response;
                }
            Printer.prompt("option does not exist!");
        }
    }
}

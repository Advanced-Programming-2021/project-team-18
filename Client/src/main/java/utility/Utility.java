package utility;

import com.google.gson.Gson;
import data.Printer;
import game.AIPlayer;
import game.Player;
import lombok.Getter;
import lombok.SneakyThrows;
import menus.ProfileResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import view.UtilityView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {
    @Getter
    private static final String SERVER_LOCATION = "http://localhost:8080";
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

    public static int getARandomNumber(int bound) {
        if (bound == 0) return -1;
        if (random.nextBoolean()) random.setSeed(LocalDateTime.now().getNano());
        return random.nextInt(bound);
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        return Pattern.compile(regex).matcher(input);
    }

    @SneakyThrows
    public static ArrayList<String[]> getArrayListFromCSV(String fileLocation) {
        File file = new File(Utility.class.getResource(fileLocation).toURI());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
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
        if (condition) {
            UtilityView.showError(promptMessage);
        }
        return condition;
    }

    @SneakyThrows
    public static String getRequest(String location, HashMap<String, String> params, HashMap<String, String> headers) {
        URIBuilder builder = new URIBuilder(location);
        if (params != null) {
            for (String param : params.keySet())
                builder = builder.setParameter(param, params.get(param));
        }
        HttpGet getRequest = new HttpGet(builder.build());
        if (headers != null) {
            for (String header : headers.keySet())
                getRequest.setHeader(header, headers.get(header));
        }
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(getRequest);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, "UTF-8");
    }

    @SneakyThrows
    public static String postRequest(String location, HashMap<String, String> params, HashMap<String, String> headers) {
        URIBuilder builder = new URIBuilder(location);
        if (params != null) {
            for (String param : params.keySet())
                builder = builder.setParameter(param, params.get(param));
        }
        HttpPost postRequest = new HttpPost(builder.build());
        if (headers != null) {
            for (String header : headers.keySet())
                postRequest.setHeader(header, headers.get(header));
        }
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(postRequest);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, "UTF-8");
    }

    public static HashMap<String, String> makeHashMap(String... args) {
        if ((args.length & 1) == 1) return null;
        HashMap<String, String> header = new HashMap<>();
        for (int i = 0; i < args.length; i += 2)
            header.put(args[i], args[i + 1]);
        return header;
    }

    public static String[] makeArray(String... args) {
        String[] result = new String[args.length];
        for (int i = 0; i < args.length; ++i)
            result[i] = args[i];
        return result;
    }

    public static String send(String location, String... args) {
        if ((args.length & 1) == 1) return null;
        HashMap<String, String> headers = makeHashMap(args);
        return getRequest(SERVER_LOCATION + location, null, headers);
    }

    public static String askPlayer(Player player, String message, ArrayList<String> options) {
        if (player instanceof AIPlayer || player == null)
            return options.get(getARandomNumber(options.size()));
        return UtilityView.obtainInformationInList(player.getUser().getUsername() + " : " + message, options.toArray(new String[0]));
    }

    public static String getJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}

package data;


import card.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import game.AIPlayer;
import game.Deck;
import game.GameDeck;
import game.User;
import lombok.SneakyThrows;
import utility.Utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataManager {
    private static final String MONSTER_CARDS_PATH = "/cards/Monster.csv";
    private static final String SPELL_AND_TRAP_CARDS_PATH = "/cards/SpellTrap.csv";
    private static final String USERS_DATA_PATH = "/users/allUsers.json";
    private static final String CARD_BALANCE_PATH = "/cards/CardBalance.json";
    private static final String FORBIDDEN_CARDS_PATH = "/cards/ForbiddenCards.json";

    public static void loadMonsterCardsIntoAllCards() {
        ArrayList<String[]> cards = Utility.getArrayListFromCSV(MONSTER_CARDS_PATH);
        String[] attributes = cards.get(0);
        for (int i = 1; i < cards.size(); ++i) {
            String[] cardInfo = cards.get(i);
            MonsterCard card = new MonsterCard();
            for (int j = 0; j < attributes.length; ++j) {
                String attribute = attributes[j];
                switch (attribute) {
                    case "Name":
                        card.setCardName(cardInfo[j]);
                        break;
                    case "Level":
                        card.setCardLevel(Integer.parseInt(cardInfo[j]));
                        break;
                    case "Attribute":
                        card.setCardAttribute(MonsterCardAttribute.valueOf(cardInfo[j].toUpperCase()));
                        break;
                    case "Monster Type":
                        card.setMonsterType(MonsterCardType.valueOf(cardInfo[j].toUpperCase()));
                        break;
                    case "Card Type":
                        card.setCardType(cardInfo[j]);
                        break;
                    case "Atk":
                        card.setCardAttack(Integer.parseInt(cardInfo[j]));
                        break;
                    case "Def":
                        card.setCardDefense(Integer.parseInt(cardInfo[j]));
                        break;
                    case "Description":
                        card.setCardDescription(cardInfo[j]);
                        break;
                    case "Price":
                        card.setPrice(Integer.parseInt(cardInfo[j]));
                        break;
                }
            }
            Card.getAllCards().add(card);
        }
    }

    public static void loadSpellCardsIntoAllCards() {
        ArrayList<String[]> cards = Utility.getArrayListFromCSV(SPELL_AND_TRAP_CARDS_PATH);
        String[] attributes = cards.get(0);
        for (int i = 1; i < cards.size(); ++i) {
            String[] cardInfo = cards.get(i);
            if (!cardInfo[1].equals("Spell"))
                continue;
            SpellCard card = new SpellCard();
            for (int j = 0; j < attributes.length; ++j) {
                String attribute = attributes[j];
                switch (attribute) {
                    case "Name":
                        card.setCardName(cardInfo[j]);
                        break;
                    case "Icon (Property)":
                        card.setCardSpellType(SpellType.valueOf(cardInfo[j].toUpperCase()));
                        break;
                    case "Description":
                        card.setCardDescription(cardInfo[j]);
                        break;
                    case "Status":
                        // not sure what status means
                        break;
                    case "Price":
                        card.setPrice(Integer.parseInt(cardInfo[j]));
                        break;
                }
            }
            Card.getAllCards().add(card);
        }
    }

    public static void loadTrapCardsIntoAllCards() {
        ArrayList<String[]> cards = Utility.getArrayListFromCSV(SPELL_AND_TRAP_CARDS_PATH);
        String[] attributes = cards.get(0);
        for (int i = 1; i < cards.size(); ++i) {
            String[] cardInfo = cards.get(i);
            if (!cardInfo[1].equals("Trap"))
                continue;
            TrapCard card = new TrapCard();
            for (int j = 0; j < attributes.length; ++j) {
                String attribute = attributes[j];
                switch (attribute) {
                    case "Name":
                        card.setCardName(cardInfo[j]);
                        break;
                    case "Icon (Property)":
                        card.setCardTrapType(TrapType.valueOf(cardInfo[j].toUpperCase()));
                        break;
                    case "Description":
                        card.setCardDescription(cardInfo[j]);
                        break;
                    case "Status":
                        // not sure what status means
                        break;
                    case "Price":
                        card.setPrice(Integer.parseInt(cardInfo[j]));
                        break;
                }
            }
            Card.getAllCards().add(card);
        }
    }

    @SneakyThrows
    private static void loadCardBalance(){
        String content = new String(Files.readAllBytes(
                Paths.get(Objects.requireNonNull(
                        DataManager.class.getResource(CARD_BALANCE_PATH)).toURI())
        ));
        Card.setCardsBalance(new Gson().fromJson(content, new TypeToken<HashMap<String, Integer>>(){}.getType()));
    }

    @SneakyThrows
    private static void loadForbiddenCards() {
        String content = new String(Files.readAllBytes(
                Paths.get(Objects.requireNonNull(
                        DataManager.class.getResource(FORBIDDEN_CARDS_PATH)).toURI())
        ));
        Card.setForbiddenCards(new Gson().fromJson(content, new TypeToken<ArrayList<String>>(){}.getType()));
    }

    @SneakyThrows
    public static void loadCardsIntoAllCards() {
        System.out.println("LOADING CARDS...");
        loadMonsterCardsIntoAllCards();
        loadSpellCardsIntoAllCards();
        loadTrapCardsIntoAllCards();
        loadCardBalance();
        loadForbiddenCards();
    }

    public static void listToMap(Deck deck) {
        deck.setCardCount(new HashMap<>());
        for (Card card : deck.getCardsList()) {
            try {
                deck.getCardCount().put(card.getCardName(), 1 + deck.getCardCount().getOrDefault(card.getCardName(), 0));
            } catch (Exception e) {
                System.out.println("couldn't save card");
            }
        }
        deck.setCardsList(null);
    }

    public static void prepareUsersDataForSaving() {
        for (User user : User.getAllUsers()) {
            for (GameDeck gameDeck : user.getDecks()) {
                listToMap(gameDeck.getMainDeck());
                listToMap(gameDeck.getSideDeck());
            }
        }
    }

    public static void mapToList(Deck deck) {
        deck.setCardsList(new ArrayList<>());
        if (deck.getCardCount() == null) return;
        for (String cardName : deck.getCardCount().keySet()) {
            int value = deck.getCardCount().get(cardName);
            for (int i = 0; i < value; ++i)
                deck.getCardsList().add(Card.getCardByName(cardName));
        }
        deck.setCardCount(null);
    }

    public static void prepareUserDataAfterLoading() {
        for (User user : User.getAllUsers()) {
            for (GameDeck gameDeck : user.getDecks()) {
                mapToList(gameDeck.getMainDeck());
                mapToList(gameDeck.getSideDeck());
            }
        }
    }

    @SneakyThrows
    public static void saveUsersData() {
        prepareUsersDataForSaving();
        File file = new File(Objects.requireNonNull(
                DataManager.class.getResource(USERS_DATA_PATH)).toURI());
        FileWriter fileWriter = new FileWriter(file);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(User.getAllUsers());
        fileWriter.write(json);
        fileWriter.close();
    }

    @SneakyThrows
    private static void saveCardBalance(){
        File file = new File(Objects.requireNonNull(
                DataManager.class.getResource(CARD_BALANCE_PATH)).toURI());
        FileWriter fileWriter = new FileWriter(file);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(Card.getCardsBalance());
        fileWriter.write(json);
        fileWriter.close();
    }

    @SneakyThrows
    private static void saveForbiddenCards() {
        File file = new File(Objects.requireNonNull(
                DataManager.class.getResource(FORBIDDEN_CARDS_PATH)).toURI());
        FileWriter fileWriter = new FileWriter(file);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(Card.getForbiddenCards());
        fileWriter.write(json);
        fileWriter.close();
    }

    public static void saveData() {
        saveUsersData();
        saveCardBalance();
        saveForbiddenCards();
    }

    @SneakyThrows
    public static void loadUsersData() {
        try {
            Gson gson = new Gson();
            String text = new String(Files.readAllBytes(Paths.get((new File(Objects.requireNonNull(
                    DataManager.class.getResource(USERS_DATA_PATH)).getPath())).getAbsolutePath())));

            ArrayList<User> users = gson.fromJson(text, new TypeToken<List<User>>() {
            }.getType());
            if (users == null) return;
            User.setAllUsers(users);
            prepareUserDataAfterLoading();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed loading users");
        }
    }

    public static void initializeAIDeck() {
        String fileContent = "";
        try {
            fileContent = new String(Files.readAllBytes((Paths.get((new File(Objects.requireNonNull(
                    DataManager.class.getResource("/cards/AIDeck")).getPath())).getAbsolutePath()))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matcher agent = Pattern.compile("([^\\n]+)\\n").matcher(fileContent);

        Deck deck = new Deck(60);
        while (agent.find()) {
            Card card = Card.getCardByName(agent.group(1).trim());
            deck.addCard(card);
        }
        AIPlayer.setAIDeck(deck);
    }
}

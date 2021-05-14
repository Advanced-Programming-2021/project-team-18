package data;


import card.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.Deck;
import game.GameDeck;
import game.User;
import lombok.SneakyThrows;
import utility.Utility;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// by Pasha

public class DataManager {
    private static final String MONSTER_CARDS_PATH = "src/main/resources/cards/Monster.csv";
    private static final String SPELLANDTRAP_CARDS_PATH = "src/main/resources/cards/SpellTrap.csv";
    private static final String USERS_DATA_PATH = "src/main/resources/users/allUsers.json";
    public static void loadMonsterCardsIntoAllCards() {
        ArrayList<String[]> cards = Utility.getArrayListFromCSV(MONSTER_CARDS_PATH);
        String[] attributes = cards.get(0);
        for(int i = 1;i < cards.size();++ i) {
            String[] cardInfo = cards.get(i);
            MonsterCard card = new MonsterCard();
            for(int j = 0;j < attributes.length;++ j) {
                String attribute = attributes[j];
                if(attribute.equals("Name")) {
                    card.setCardName(cardInfo[j]);
                } else if(attribute.equals("Level")) {
                    card.setCardLevel(Integer.parseInt(cardInfo[j]));
                } else if(attribute.equals("Attribute")) {
                    card.setCardAttribute(MonsterCardAttribute.valueOf(cardInfo[j].toUpperCase()));
                } else if(attribute.equals("Monster Type")) {
                    card.setMonsterType(MonsterCardType.valueOf(cardInfo[j].toUpperCase()));
                } else if(attribute.equals("Card Type")){
                    card.setCardType(cardInfo[j]);
                } else if(attribute.equals("Atk")) {
                    card.setCardAttack(Integer.parseInt(cardInfo[j]));
                } else if(attribute.equals("Def")) {
                    card.setCardDefense(Integer.parseInt(cardInfo[j]));
                } else if(attribute.equals("Description")) {
                    card.setCardDescription(cardInfo[j]);
                } else if(attribute.equals("Price")) {
                    card.setPrice(Integer.parseInt(cardInfo[j]));
                }
            }
            Card.getAllCards().add(card);
        }
    }
    public static void loadSpellCardsIntoAllCards() {
        ArrayList<String[]> cards = Utility.getArrayListFromCSV(SPELLANDTRAP_CARDS_PATH);
        String[] attributes = cards.get(0);
        for(int i = 1;i < cards.size();++ i) {
            String[] cardInfo = cards.get(i);
            if(!cardInfo[1].equals("Spell"))
                continue ;
            SpellCard card = new SpellCard();
            for(int j = 0;j < attributes.length;++ j) {
                String attribute = attributes[j];
                if(attribute.equals("Name")) {
                    card.setCardName(cardInfo[j]);
                } else if(attribute.equals("Icon (Property)")) {
                    card.setCardSpellType(SpellType.valueOf(cardInfo[j].toUpperCase()));
                } else if(attribute.equals("Description")) {
                    card.setCardDescription(cardInfo[j]);
                } else if(attribute.equals("Status")) {
                    // not sure what status means
                } else if(attribute.equals("Price")) {
                    card.setPrice(Integer.parseInt(cardInfo[j]));
                }
            }
            Card.getAllCards().add(card);
        }
    }
    public static void loadTrapCardsIntoAllCards() {
        ArrayList<String[]> cards = Utility.getArrayListFromCSV(SPELLANDTRAP_CARDS_PATH);
        String[] attributes = cards.get(0);
        for(int i = 1;i < cards.size();++ i) {
            String[] cardInfo = cards.get(i);
            if(!cardInfo[1].equals("Trap"))
                continue ;
            TrapCard card = new TrapCard();
            for(int j = 0;j < attributes.length;++ j) {
                String attribute = attributes[j];
                if(attribute.equals("Name")) {
                    card.setCardName(cardInfo[j]);
                } else if(attribute.equals("Icon (Property)")) {
                    card.setCardTrapType(TrapType.valueOf(cardInfo[j].toUpperCase()));
                } else if(attribute.equals("Description")) {
                    card.setCardDescription(cardInfo[j]);
                } else if(attribute.equals("Status")) {
                    // not sure what status means
                } else if(attribute.equals("Price")) {
                    card.setPrice(Integer.parseInt(cardInfo[j]));
                }
            }
            Card.getAllCards().add(card);
        }
    }
    public static void loadCardsIntoAllCards() {
        loadMonsterCardsIntoAllCards();
        loadSpellCardsIntoAllCards();
        loadTrapCardsIntoAllCards();
    }
    public static void listToMap(Deck deck) {
        deck.setCardCount(new HashMap<>());
        for(Card card : deck.getCardsList())
            deck.getCardCount().put(card.getCardName() , 1 + deck.getCardCount().getOrDefault(card.getCardName() , 0));
        deck.setCardsList(null);
    }
    public static void prepareUsersDataForSaving() {
        for(User user : User.getAllUsers()) {
            for(GameDeck gameDeck : user.getDecks()) {
                listToMap(gameDeck.getMainDeck());
                listToMap(gameDeck.getSideDeck());
            }
        }
    }
    public static void mapToList(Deck deck) {
        if(deck.getCardCount() == null) return ;
        deck.setCardsList(new ArrayList<>());
        for(String cardName : deck.getCardCount().keySet()) {
            int value = deck.getCardCount().get(cardName);
            for(int i = 0;i < value;++ i)
                deck.getCardsList().add(Card.getCardByName(cardName));
        }
        deck.setCardCount(null);
    }
    public static void prepareUserDataAfterLoading() {
        for(User user : User.getAllUsers()) {
            for(GameDeck gameDeck : user.getDecks()) {
                mapToList(gameDeck.getMainDeck());
                mapToList(gameDeck.getSideDeck());
            }
        }
    }

    @SneakyThrows
    public static void saveUsersData() {

        prepareUsersDataForSaving();
        FileWriter fileWriter = new FileWriter(USERS_DATA_PATH);
        Gson gson = new Gson();
        String json = gson.toJson(User.getAllUsers());
        fileWriter.write(json);
        fileWriter.close();
    }
    @SneakyThrows
    public static void loadUsersData() {
        try {
            Gson gson = new Gson();
            String text = new String(Files.readAllBytes(Paths.get(USERS_DATA_PATH)));
            ArrayList < User > users = gson.fromJson(text , new TypeToken<List<User>>(){}.getType());
            User.setAllUsers(users);
            prepareUserDataAfterLoading();
        } catch(Exception e) {
            // file not found not a big deal
        }
    }
}

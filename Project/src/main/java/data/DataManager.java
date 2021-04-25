package data;


import card.*;
import game.User;
import lombok.SneakyThrows;
import utility.Utility;

import java.io.*;
import java.util.ArrayList;


// TODO : PASHA

public class DataManager {
    private static final String MONSTER_CARDS_PATH = "src\\main\\resources\\cards\\Monster.csv";
    private static final String SPELLANDTRAP_CARDS_PATH = "src\\main\\resources\\cards\\SpellTrap.csv";
    private static final String USERS_DATA_DIRECTORY = "src\\main\\resources\\users\\allUsers.txt";
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
    @SneakyThrows
    public static void saveUsersData() {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_DATA_DIRECTORY));
        out.writeObject(User.getAllUsers());
    }
    @SneakyThrows
    public static void loadUsersData() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(USERS_DATA_DIRECTORY));
            User.setAllUsers((ArrayList<User>) in.readObject());
        } catch(FileNotFoundException e) {
            // file not found not a big deal
        }
    }
}

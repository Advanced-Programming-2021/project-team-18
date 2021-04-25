package data;


import card.Card;
import card.MonsterCard;
import card.MonsterCardAttribute;
import card.MonsterCardType;
import utility.Utility;
import java.util.ArrayList;
import java.util.Locale;

// TODO : PASHA

public class DataManager {
    private static final String MONSTER_CARDS_PATH = "src\\main\\resources\\Monster.csv";

    public static void loadCardsIntoAllCards() {
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

    public static void saveData() {

    }

    public static void loadData() {

    }
}

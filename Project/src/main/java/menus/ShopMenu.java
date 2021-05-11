package menus;

import card.Card;
import data.Printer;
import game.User;
import utility.Utility;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;

// By Sina
public class ShopMenu extends Menu {
    private static final HashMap<String, Integer> cardsPrice = new HashMap<>();
    private static final TreeSet<String> allCardNames = new TreeSet<>();
    private static final String name = "Shop Menu";
    private final User user;
    // Note: there are two kinds of showing a card: shop-based and full-detailed
    // since there is no attribute about "price" in card, the first one should be handled
    // in ShopMenu and DeckMenu separately

    public ShopMenu(User user) {
        this.user = user;
        if (cardsPrice.isEmpty()){
            for (Card card : Card.getAllCards()) {
                cardsPrice.put(card.getCardName(), card.getPrice());
                allCardNames.add(card.getCardName());
            }
        }
    }

    private void showCardAndPrice(String cardName) {
        Printer.prompt(cardName + ":" + cardsPrice.get(cardName));
    }

    private void showAllCardsAndPrices() {
        for (String cardName : allCardNames)
            showCardAndPrice(cardName);
    }

    private void sellCard(String cardName) {
        if (!allCardNames.contains(cardName)) {
            Printer.prompt("there is no card with this name");
            return;
        }
        Integer price = cardsPrice.get(cardName);
        if (user.getBalance() < price) {
            Printer.prompt("not enough money");
            return;
        }
        user.decreaseBalance(price);
        user.addCardBalance(cardName);
    }

    @Override
    public void runMenu() {
        String newLine = Utility.getNextLine();
        Matcher matcher;
        while (!newLine.equals(MENU_EXIT)) {
            if (newLine.equals(SHOW_MENU)) Printer.prompt(name);
            else if ((matcher = Utility.getCommandMatcher(newLine, "shop buy +(.+) *")).matches()) {
                sellCard(matcher.group(1));
            } else if (newLine.equals("shop show --all")) {
                showAllCardsAndPrices();
            } else if (newLine.matches("enter menu .*")) {
                Printer.prompt(NAVIGATION_DENIED);
            } else Printer.prompt(INVALID_COMMAND);
            newLine = Utility.getNextLine();
        }
    }
}

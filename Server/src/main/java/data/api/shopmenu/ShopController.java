package data.api.shopmenu;

import card.Card;
import game.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopController {
    @RequestMapping(path = "api/shopmenu/get_user_balance")
    @GetMapping
    public String getUserBalance(@RequestHeader(value = "token") String token) {
        System.out.println("getUserBalance Called with token: " + token);
        User user = User.getUserByToken(token);
        if (user == null) {
            System.out.println("NO SUCH USER EXISTS! I'LL RETURN NULL");
            return null;
        }
        System.out.println("I'll return " + user.getBalance());
        return String.valueOf(user.getBalance());
    }

    @RequestMapping(path = "api/shopmenu/get_card_balance")
    @GetMapping
    public String getCardBalance(@RequestHeader(value = "token") String token,
                                 @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        if (user == null) return null;
        Card card = Card.getCardByName(cardName);
        if (card == null) return null;
        return String.valueOf(user.getCardBalance(cardName));
    }

    @RequestMapping(path = "api/shopmenu/buy_card")
    @GetMapping
    public String buyCard(@RequestHeader(value = "token") String token,
                          @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        if (user == null) return "username_not_found";
        Card card = Card.getCardByName(cardName);
        if (card == null) return "card_not_found";
        if (user.getBalance() < card.getPrice()) return "insufficient_balance";
        Integer cardBalance = Card.getBalanceOfCard(cardName);
        System.out.println("cardBalance = " + cardBalance);
        if (cardBalance == 0) return "no_more_card";
        Card.decreaseBalanceOfCard(cardName);
        System.out.println("cardBalance (after modificaiton) = " + Card.getCardsBalance().get(cardName));
        user.decreaseBalance(card.getPrice());
        user.addCardBalance(cardName);
        return "successful";
    }

    @RequestMapping(path = "api/shopmenu/sell_card")
    @GetMapping
    public String sellCard(@RequestHeader(value = "token") String token,
                           @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        if (user == null) return "username_not_found";
        Card card = Card.getCardByName(cardName);
        if (card == null) return "card_not_found";
        if (user.getCardBalance(cardName) == 0) return "insufficient_balance";
        Card.increaseBalanceOfCard(cardName);
        System.out.println("cardBalance (after modification) = " + Card.getCardsBalance().get(cardName));
        user.increaseBalance(card.getPrice());
        user.subtractCardBalance(cardName);
        return "successful";
    }
}

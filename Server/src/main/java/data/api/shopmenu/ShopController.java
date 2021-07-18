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
    public void buyCard(@RequestHeader(value = "token") String token,
                        @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        if (user == null) return;
        Card card = Card.getCardByName(cardName);
        if (card == null) return;
        if (user.getBalance() < card.getPrice()) return;
        user.decreaseBalance(card.getPrice());
        user.addCardBalance(cardName);
    }
}

package game;

import card.Card;
import lombok.Getter;
import lombok.Setter;
import utility.Utility;
import view.UtilityView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// By Sina
public class User implements Comparable<User>, Serializable {
    @Setter
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static final User dummyUser = new User();

    @Setter
    @Getter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String nickname;
    @Getter
    @Setter
    private int score;
    @Getter
    @Setter
    private String activeDeckName;
    @Getter
    @Setter
    private int balance;
    @Getter
    @Setter
    private int avatarID;
    private final ArrayList<GameDeck> decks;
    private final HashMap<String, Integer> cardCount;

    public static List<User> getAllUsers() {
        return Collections.unmodifiableList(allUsers);
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.username.equals(username)) return user;
        }
        return null;
    }

    public static boolean isNicknameTaken(String nickname) {
        for (User user : allUsers) {
            if (user.nickname.equals(nickname)) return true;
        }
        return false;
    }

    public static void removeUser(String username) {
        for (User user : allUsers) {
            if (user.getUsername().contentEquals(username)) {
                allUsers.remove(user);
                return;
            }
        }
    }

    public static User getDummyUser() {
        return dummyUser;
    }


    private User() {
        username = "The Computer";
        password = null;
        nickname = "AI";
        balance = 0;
        score = 0;
        decks = new ArrayList<>();
        cardCount = new HashMap<>();
    }

    public User(String username, String password, String nickname) {
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        setBalance(100 * 1000);
        cardCount = new HashMap<>();
        decks = new ArrayList<>();
        GameDeck defaultDeck = new GameDeck("default");
        defaultDeck.setMainDeck(AIPlayer.getAIDeck());
        defaultDeck.setSideDeck(new Deck(15));
        decks.add(defaultDeck);
        allUsers.add(this);
//        avatarID = Utility.getARandomNumber(UtilityView.getAvatarNumbers()) + 1;
    }


    public GameDeck getGameDeckByName(String name) {
        for (GameDeck gameDeck : decks) {
            if (gameDeck.getName().equals(name)) return gameDeck;
        }
        return null;
    }

    public void addGameDeck(GameDeck deck) {
        decks.add(deck);
    }

    public void removeGameDeck(GameDeck deck) {
        if (deck.getName().equals(activeDeckName)) activeDeckName = null;
        decks.remove(deck);
    }

    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public void increaseBalance(int amount) {
        balance += amount;
    }

    // Returns true iff user did not have enough balance
    public void decreaseBalance(int amount) {
        balance -= amount;
        if (balance < 0) {
            balance = 0;
        }
    }

    public void addCardBalance(String cardName) {
        cardCount.put(cardName, cardCount.getOrDefault(cardName, 0) + 1);
    }

    public int getCardBalance(String cardName) {
        return cardCount.getOrDefault(cardName, 0);
    }

    public int getCardBalance(Card card) {
        if (card == null) return 0;
        return getCardBalance(card.getCardName());
    }

    public List<GameDeck> getDecks() {
        return Collections.unmodifiableList(decks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername()) && getPassword().equals(user.getPassword());
    }

    @Override
    public int compareTo(User anotherUser) {
        if (this.score > anotherUser.score) return -1;
        if (this.score < anotherUser.score) return 1;
        return this.nickname.compareTo(anotherUser.nickname);
    }
}

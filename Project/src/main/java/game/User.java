package game;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

// By Sina
public class User implements Comparable<User>, Serializable {
    @Setter
    private static ArrayList<User> allUsers = new ArrayList<>();

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
            if(user.getUsername().contentEquals(username)){
                allUsers.remove(user);
                return;
            }
        }
    }


    public User(String username, String password, String nickname) {
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        setBalance(100 * 1000);
        cardCount = new HashMap<>();
        decks = new ArrayList<>();
        allUsers.add(this);
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

    public boolean removeGameDeck(GameDeck deck) {
        if (deck.getName().equals(activeDeckName)) activeDeckName = null;
        return decks.remove(deck);
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
    public boolean decreaseBalance(int amount) {
        balance -= amount;
        if (balance < 0) {
            balance = 0;
            return true;
        }
        return false;
    }

    public void addCardBalance(String cardName) {
        cardCount.put(cardName, cardCount.getOrDefault(cardName, 0) + 1);
    }

    public int getCardBalance(String cardName) {
        return cardCount.getOrDefault(cardName, 0);
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
        if (this.score > anotherUser.score) return 1;
        if (this.score < anotherUser.score) return -1;
        return this.nickname.compareTo(anotherUser.nickname);
    }
}

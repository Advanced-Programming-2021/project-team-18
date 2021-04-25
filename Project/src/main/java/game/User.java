package game;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class User implements Comparable<User>{
    private static ArrayList<User> allUsers = new ArrayList<>();
    // Note: the information about users should be extracted at start time
    private String username;
    private String password;
    private String nickname;
    private int score;
    private GameDeck activeDeck;
    private int balance;
    private ArrayList<GameDeck> decks;
    private HashMap<String,Integer> cardCount;

    public User(String username, String password, String nickname) {
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        setBalance(100 * 1000);
        cardCount = new HashMap<String,Integer>();
        decks = new ArrayList<GameDeck>();
        allUsers.add(this);
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
    public static ArrayList<User> getAllUsers() { return allUsers; }

    public void addGameDeck(GameDeck deck) {
        decks.add(deck);
    }

    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    public void updateUsersData() {
        // TODO
        // NOTE: WHAT IS THIS FUNCTION?! WHY IT'S NOT STATIC?!
    }

    public GameDeck getGameDeckByName(String name) {
        for (GameDeck gameDeck : decks) {
            if (gameDeck.getName().equals(name)) return gameDeck;
        }
        return null;
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

    @Override
    public int compareTo(User anotherUser) {
        if (this.score > anotherUser.score) return 1;
        if (this.score < anotherUser.score) return -1;
        return this.nickname.compareTo(anotherUser.nickname);
    }
}

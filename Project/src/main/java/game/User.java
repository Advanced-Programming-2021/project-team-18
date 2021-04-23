package game;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class User {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int balance;
    private GameDeck activeDeck;
    private ArrayList<GameDeck> decks;
    private ArrayList<User> allUsers;

    public User(String username, String password, String nickname) {
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        allUsers.add(this);
    }
    public static User getUserByUsername(String username) {
        for (User user: allUsers) {
            if (user.username.equals(username)) return user;
        }
        return null;
    }

    public static boolean isNicknameTaken(String nickname) {
        for (User user: allUsers) {
            if (user.nickname.equals(nickname)) return true;
        }
        return false;
    }

    public void addGameDeck(GameDeck deck) {
        decks.add(deck);
    }
    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }
    public void updateUsersData() {

    }
    public GameDeck getGameDeckByName(String name) {
        for (GameDeck gameDeck: decks) {
            if (gameDeck.getName().equals(name)) return gameDeck;
        }
        return null;
    }
}

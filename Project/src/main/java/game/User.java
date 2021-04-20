package game;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
// TODO : SINA
@Getter
@Setter
public class User {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int balance;
    private GameDeck activeDeck;
    private ArrayList<GameDeck> decksList;
    private ArrayList<User> userList;

    public User(String username, String password, String nickname) {

    }
    public static User getUserByUsername(String username) {

        return null;
    }
    public void addGameDeck(Deck deck) {

    }
    public boolean isPasswordCorrect(String password) {

        return false;
    }
    public void updateUsersData() {

    }
    public GameDeck getGameDeckByName(String name) {

        return null;
    }
}

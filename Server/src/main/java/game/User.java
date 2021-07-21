package game;

import card.Card;
import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import utility.Utility;
import view.UtilityView;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

// By Sina
public class User implements Comparable<User>, Serializable {
    @Setter
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static final User dummyUser = new User();
    @Setter
    @Getter
    @Expose
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    @Expose
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
    @Expose
    private int avatarID;
    @Setter
    @Getter
    @Expose
    private String token;
    private final ArrayList<GameDeck> decks;
    private final HashMap<String, Integer> cardCount;
    @Getter
    private final ArrayList<String> messages = new ArrayList<>();
    @Getter
    private final ArrayList<Question> questions = new ArrayList<>();


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

    public static User getUserByToken(@NotNull String token) {
        for (User user : allUsers)
            if (user.getToken().equals(token))
                return user;
        return null;
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
        avatarID = 0;
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
        avatarID = Utility.getARandomNumber(UtilityView.getAvatarNumbers()) + 1;
    }

    public static int getOnlineCount() {
        int count = 0;
        for (User user : allUsers) {
            if (user.isOnline()) count++;
        }
        return count;
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

    public boolean isOnline() {
        return token != null;
    }

    public void addCardBalance(String cardName) {
        cardCount.put(cardName, cardCount.getOrDefault(cardName, 0) + 1);
    }

    public void subtractCardBalance(String cardName) {
        cardCount.put(cardName, cardCount.getOrDefault(cardName, 0) - 1);
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

    public Image getAvatar() {
        if (avatarID != -1) return UtilityView.getAvatarImage(avatarID);
        String avatarPath = Objects.requireNonNull(getClass().getResource(
                "/avatars")).toExternalForm() + "/arbitrary/" + username;
        return new Image(avatarPath);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void updateAvatar(File src) throws Exception {
        File avatarFolder = new File(new URI(Objects.requireNonNull(User.class.getResource("/avatars")) + "/arbitrary/"));
        avatarFolder.mkdirs();
        File file = new File(new URI(Objects.requireNonNull(User.class.getResource("/avatars"))
                + "/arbitrary/" + username));
        file.createNewFile();
        File dest = new File(new URI(Objects.requireNonNull(User.class.getResource("/avatars"))
                + "/arbitrary/" + username));
        Files.copy(Paths.get(src.toURI()), Paths.get(dest.toURI()), REPLACE_EXISTING);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        if (user == User.getDummyUser()) return false;
        return getUsername().equals(user.getUsername()) && getPassword().equals(user.getPassword());
    }

    @Override
    public int compareTo(User anotherUser) {
        if (this.score > anotherUser.score) return -1;
        if (this.score < anotherUser.score) return 1;
        return this.nickname.compareTo(anotherUser.nickname);
    }
}

package game;

import java.util.ArrayList;

public class User {
    private String username;		
    private String password;		
    private String nickname;		
    private int score;		
    private int balance;		
    private ArrayList<GameDeck> decksList;
    private ArrayList<User> userList;

    public User(String username, String password, String nickname) {
        
    }		
    
    public String getUsername() {

        return "";
    }		
    
    public boolean isPasswordCorrect(String password) {

        return false;
    }		
    
    public void setPassword(String password) {
        
    }		
    
    public String getNickname() {

        return "";
    }		
    
    public void setNickname(String nickname) {
        
    }		
}

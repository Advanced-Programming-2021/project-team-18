package view.menu.scoreboard;

import game.User;

public class SimplifiedUser {
    private int score;
    private String username, nickname;
    private boolean isOnline;

    public SimplifiedUser(int score, String username, String nickname, boolean isOnline) {
        this.score = score;
        this.username = username;
        this.isOnline = isOnline;
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getNickname() {
        return nickname;
    }
}

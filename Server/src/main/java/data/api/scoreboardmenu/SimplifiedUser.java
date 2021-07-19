package data.api.scoreboardmenu;

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

    public static SimplifiedUser constructFromUser(User user) {
        return new SimplifiedUser(user.getScore(), user.getUsername(),user.getNickname(), user.isOnline());
    }
}

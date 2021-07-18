package data.api.duelmenu;

import game.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private User firstUser;
    private User secondUser;
    public Request(User firstUser , User secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }
}

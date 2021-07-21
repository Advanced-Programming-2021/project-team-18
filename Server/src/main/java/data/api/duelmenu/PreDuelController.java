package data.api.duelmenu;

import game.Game;
import game.GameDeck;
import game.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Random;

@RestController
public class PreDuelController {
    private static ArrayList<Request> notSeenRequests = new ArrayList<>();
    private static ArrayList<Request> seenRequests = new ArrayList<>();

    @RequestMapping(path = "api/duelmenu/pre_duel/request_duel", method = RequestMethod.POST)
    @PostMapping
    public String requestDuel(@RequestHeader(value = "token") String token, @RequestHeader(value = "opponent") String username) {
        User firstUser = User.getUserByToken(token);
        User secondUser = User.getUserByUsername(username);
        if (firstUser == null || secondUser == null)
            return "no user exists with this username";
        if (secondUser == firstUser)
            return "you can't duel with yourself";
        if (firstUser.getActiveDeckName() == null || secondUser.getActiveDeckName() == null)
            return "you or your opponent don't have an active deck";
        Request request = new Request(firstUser, secondUser);
        notSeenRequests.add(request);
        return "request was sent to opponent";
    }

    @RequestMapping(path = "api/duelmenu/pre_duel/check_request", method = RequestMethod.GET)
    @GetMapping
    public String checkRequest(@RequestHeader(value = "token") String token) {
        User user = User.getUserByToken(token);
        Request selectedRequest = null;
        for (Request request : notSeenRequests)
            if (request.getSecondUser().getUsername().equals(user.getUsername())) {
                selectedRequest = request;
                break;
            }
        if (selectedRequest == null)
            return "no user was found";
        notSeenRequests.remove(selectedRequest);
        seenRequests.add(selectedRequest);
        return selectedRequest.getFirstUser().getUsername();
    }

    @RequestMapping(path = "api/duelmenu/pre_duel/accept_request", method = RequestMethod.POST)
    @PostMapping
    public void acceptRequest(@RequestHeader(value = "token") String token) {
        User user = User.getUserByToken(token);
        Request selectedRequest = null;
        for (Request request : seenRequests)
            if (request.getSecondUser().getUsername().equals(user.getUsername()))
                selectedRequest = request;
        if (selectedRequest == null)
            return;
        seenRequests.remove(selectedRequest);
        boolean turn = (new Random(System.nanoTime())).nextBoolean();
        createGame(turn, selectedRequest);
        selectedRequest.getFirstUser().getMessages().add("game incoming");
        selectedRequest.getSecondUser().getMessages().add("game incoming");
    }

    @RequestMapping(path = "api/duelmenu/pre_duel/receive_last_message", method = RequestMethod.GET)
    @GetMapping
    public String receiveLastMessage(@RequestHeader(value = "token") String token) {
        User user = User.getUserByToken(token);
        if (user == null) return null;
        if (user.getMessages().isEmpty()) return "no message was found";
        return user.getMessages().remove(0);
    }

    private void createGame(boolean turn, Request request) {
        Game game;
        if (turn) game = new Game(request.getFirstUser(), request.getSecondUser(), 1);
        else game = new Game(request.getSecondUser(), request.getFirstUser(), 1);
        game.runGame();
        System.out.println(game.getFirstPlayer().getUser().getUsername());
        System.out.println(game.getSecondPlayer().getUser().getUsername());
        Game.getGames().add(game);
    }
}

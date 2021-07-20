package data.api.duelmenu;

import card.Card;
import card.CardSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Game;
import game.Player;
import game.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameMenuController {

    @RequestMapping(path = "api/duelmenu/game_menu/get_game_by_token" , method = RequestMethod.GET)
    @GetMapping
    public String refresh(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Card.class , new CardSerializer()).create();
        return gson.toJson(game);
    }
    @RequestMapping(path = "api/duelmenu/game_menu/should_refresh" , method = RequestMethod.GET)
    @GetMapping
    public String shouldRefresh(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        if(player.isShouldRefresh()) {
            player.setShouldRefresh(false);
            return "yes";
        }
        return "no";
    }
    @RequestMapping(path = "api/duelmenu/game_menu/select_card" , method = RequestMethod.POST)
    @PostMapping
    public void selectCard(@RequestHeader(value = "token") String token , @RequestHeader(value = "command") String command) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        String[] splittedCommand = command.split(" ");
        Player who = (splittedCommand[0].equals("me") ? player : player.getOpponent());
        String where = splittedCommand[1];
        int index = Integer.parseInt(splittedCommand[2]);
        if(where.equals("field"))
            player.setSelectedCard(who.getFieldZone());
        else if(where.equals("hand"))
            player.setSelectedCard(who.getHand().getCardsList().get(index));
        else if(where.equals("spell"))
            player.setSelectedCard(who.getSpellsAndTrapFieldList()[index]);
        else
            player.setSelectedCard(who.getMonstersFieldList()[index]);
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/next_phase" , method = RequestMethod.POST)
    @PostMapping
    public void nextPhase(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        game.proceedNextPhase();
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/summon_monster" , method = RequestMethod.POST)
    @PostMapping
    public void summonMonster(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        player.summonMonster();
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/set_monster" , method = RequestMethod.POST)
    @PostMapping
    public void setMonster(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        player.setMonster();
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/change_position" , method = RequestMethod.POST)
    @PostMapping
    public void ChangePosition(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        player.changeMonsterPosition();
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/flip_summon" , method = RequestMethod.POST)
    @PostMapping
    public void flipSummon(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        player.flipSummon();
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/attack_direct" , method = RequestMethod.POST)
    @PostMapping
    public void attackDirect(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        player.attackDirect();
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/attack_monster" , method = RequestMethod.POST)
    @PostMapping
    public void attackMonster(@RequestHeader(value = "token") String token  , @RequestHeader(value = "position") String position) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        player.attack(Integer.parseInt(position));
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/set_spell_or_trap" , method = RequestMethod.POST)
    @PostMapping
    public void setSpellOrTrap(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        player.setSpellOrTrap();
        game.notifyGraphic();
    }
    @RequestMapping(path = "api/duelmenu/game_menu/activate_effect" , method = RequestMethod.POST)
    @PostMapping
    public void activateEffect(@RequestHeader(value = "token") String token) {
        Game game = Game.getGameByToken(token);
        User user = User.getUserByToken(token);
        Player player = game.getPlayerByUser(user);
        player.activateEffect();
        game.notifyGraphic();
    }

}

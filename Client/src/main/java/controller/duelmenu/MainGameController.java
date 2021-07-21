package controller.duelmenu;

import card.Card;
import card.CardSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Game;
import utility.Utility;

public class MainGameController {
    private static final String GET_GAME_BY_TOKEN = "/api/duelmenu/game_menu/get_game_by_token";
    private static final String SHOULD_REFRESH = "/api/duelmenu/game_menu/should_refresh";
    private static final String SELECT_CARD = "/api/duelmenu/game_menu/select_card";
    private static final String NEXT_PHASE = "/api/duelmenu/game_menu/next_phase";
    private static final String SUMMON_MONSTER = "/api/duelmenu/game_menu/summon_monster";
    private static final String SET_MONSTER = "/api/duelmenu/game_menu/set_monster";
    private static final String CHANGE_POSITION = "/api/duelmenu/game_menu/change_position";
    private static final String FLIP_SUMMON = "/api/duelmenu/game_menu/flip_summon";
    private static final String ATTACK_DIRECT = "/api/duelmenu/game_menu/attack_direct";
    private static final String ATTACK_MONSTER = "/api/duelmenu/game_menu/attack_monster";
    private static final String SET_SPELL_OR_TRAP = "/api/duelmenu/game_menu/set_spell_or_trap";
    private static final String ACTIVATE_EFFECT = "/api/duelmenu/game_menu/activate_effect";
    private static final String FORFEIT = "/api/duelmenu/game_menu/forfeit";

    public static Game getGameByToken(String token) {
        Gson gson = (new GsonBuilder()).registerTypeAdapter(Card.class, new CardSerializer()).create();
        String response = Utility.getRequest(Utility.getSERVER_LOCATION() + GET_GAME_BY_TOKEN, null, Utility.makeHashMap("token", token));
        Game game = gson.fromJson(response, Game.class);
        return game;
    }

    public static boolean shouldRefresh(String token) {
        String response = Utility.getRequest(Utility.getSERVER_LOCATION() + SHOULD_REFRESH, null, Utility.makeHashMap("token", token));
        return response.equals("yes");
    }

    public static void selectCard(String token, String command) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + SELECT_CARD, null, Utility.makeHashMap("token", token, "command", command));
    }

    public static void nextPhase(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + NEXT_PHASE, null, Utility.makeHashMap("token", token));
    }

    public static void summonMonster(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + SUMMON_MONSTER, null, Utility.makeHashMap("token", token));
    }

    public static void setMonster(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + SET_MONSTER, null, Utility.makeHashMap("token", token));
    }

    public static void changePosition(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + CHANGE_POSITION, null, Utility.makeHashMap("token", token));
    }

    public static void flipSummon(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + FLIP_SUMMON, null, Utility.makeHashMap("token", token));
    }

    public static void attackDirect(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + ATTACK_DIRECT , null , Utility.makeHashMap("token" , token));
    }

    public static void attackMonster(String token, int position) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + ATTACK_MONSTER , null , Utility.makeHashMap("token" , token , "position" , position + ""));
    }

    public static void setSpellOrTrap(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + SET_SPELL_OR_TRAP, null , Utility.makeHashMap("token" , token));
    }

    public static void activateEffect(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + ACTIVATE_EFFECT , null , Utility.makeHashMap("token" , token));
    }
    public static void forfeit(String token) {
        Utility.postRequest(Utility.getSERVER_LOCATION() + FORFEIT , null , Utility.makeHashMap("token" , token));
    }
}

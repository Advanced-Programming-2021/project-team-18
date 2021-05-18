package effects;

import card.Card;
import card.MonsterCard;
import events.*;
import game.Player;

public class SwordsOfRevealingLightEffect extends Effect {
    private Player player;
    private int numberOfTurns;
    private int sourceCardOnFieldPlace;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            Card sourceCard = cardEvent.getCard();
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            if ((cardEventInfo == CardEventInfo.ENTRANCE && sourceCard.hasEffect(this) && player == null)
                    || (cardEventInfo == CardEventInfo.FLIP && sourceCard.hasEffect(this) & player == null)) {
                player = sourceCard.getPlayer();
                numberOfTurns = 0;
                sourceCardOnFieldPlace = player.getSpellOrTrapPositionOnBoard(sourceCard);
                Player opponent = player.getOpponent();
                MonsterCard[] monstersField = opponent.getMonstersFieldList();
                for (int i = 1; i <= Player.getFIELD_SIZE(); i++) {
                    if (monstersField[i] != null && !monstersField[i].isFaceUp()) {
                        //Permit from everyone
                        monstersField[i].setFaceUp(true);
                    }
                }
            }
        } else if (event instanceof AttackEvent) {
            Card attacker = ((AttackEvent) event).getAttacker();
            Card defender = ((AttackEvent) event).getDefender();
            Player attackerPlayer = attacker.getPlayer();
            if (attackerPlayer.equals(player.getOpponent())) return false;
        } else if (event instanceof TurnChangeEvent) {
            if (((TurnChangeEvent) event).getPlayer().equals(player.getOpponent())) {
                numberOfTurns++;
                if (numberOfTurns == 3) {
                    //Permit for destroy
                    Card sourceCard = player.getSpellsAndTrapFieldList()[sourceCardOnFieldPlace];
                    player.getSpellsAndTrapFieldList()[sourceCardOnFieldPlace] = null;
                    player.getGraveyard().addCard(sourceCard);
                }
            }
        }
        return true;
    }
}

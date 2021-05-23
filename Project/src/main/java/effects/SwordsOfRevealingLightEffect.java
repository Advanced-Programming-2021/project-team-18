package effects;

import card.Card;
import card.MonsterCard;
import events.*;
import game.Player;

public class SwordsOfRevealingLightEffect extends Effect {
    private int numberOfTurns;
    private int sourceCardOnFieldPlace;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            Card sourceCard = cardEvent.getCard();
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            if ((cardEventInfo == CardEventInfo.ENTRANCE && sourceCard.hasEffect(this) && selfPlayer == null)
                    || (cardEventInfo == CardEventInfo.FLIP && sourceCard.hasEffect(this) & selfPlayer == null)) {
                selfPlayer = sourceCard.getPlayer();
                numberOfTurns = 0;
                sourceCardOnFieldPlace = selfPlayer.getSpellOrTrapPositionOnBoard(sourceCard);
                Player opponent = selfPlayer.getOpponent();
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
            if (attackerPlayer.equals(selfPlayer.getOpponent())) return false;
        } else if (event instanceof TurnChangeEvent) {
            if (((TurnChangeEvent) event).getPlayer().equals(selfPlayer.getOpponent())) {
                numberOfTurns++;
                if (numberOfTurns == 3) {
                    //Permit for destroy
                    Card sourceCard = selfPlayer.getSpellsAndTrapFieldList()[sourceCardOnFieldPlace];
                    selfPlayer.getSpellsAndTrapFieldList()[sourceCardOnFieldPlace] = null;
                    selfPlayer.getGraveyard().addCard(sourceCard);
                }
            }
        }
        return true;
    }

    public void consider(Event event) {

    }
}

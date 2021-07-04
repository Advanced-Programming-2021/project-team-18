package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.*;
import game.Player;

public class SwordsOfRevealingLightEffect extends Effect {
    private int numberOfTurns;
    private boolean isActivated = false;

    public void runEffect() {
        MonsterCard[] monstersField = selfPlayer.getOpponent().getMonstersFieldList();
        for (int i = 1; i <= Player.getFIELD_SIZE(); i++) {
            if (monstersField[i] != null && !monstersField[i].isFaceUp()) {
                CardEvent flipEvent = new CardEvent(monstersField[i], CardEventInfo.FLIP, selfCard);
                if (selfPlayer.getPermissionFromAllEffects(flipEvent)) {
                    monstersField[i].setFaceUp(true);
                    selfPlayer.notifyAllEffectsForConsideration(flipEvent);
                } else {
                    Printer.prompt("an effect prevented flip!");
                }
            }
        }
    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            Card sourceCard = cardEvent.getCard();
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            if ((cardEventInfo == CardEventInfo.ACTIVATE_EFFECT && sourceCard.hasEffect(this) && selfPlayer == null)
                    || (cardEventInfo == CardEventInfo.FLIP && sourceCard.hasEffect(this) & selfPlayer == null)) {
                selfPlayer = sourceCard.getPlayer();
                numberOfTurns = 0;
                selfCard = sourceCard;
                isActivated = true;
                runEffect();
            }
        } else if (event instanceof AttackEvent && isActivated) {
            Card attacker = ((AttackEvent) event).getAttacker();
            Card defender = ((AttackEvent) event).getDefender();
            Player attackerPlayer = attacker.getPlayer();
            if (attackerPlayer.equals(selfPlayer.getOpponent())) return false;
        } else if (event instanceof TurnChangeEvent && isActivated) {
            if (((TurnChangeEvent) event).getPlayer().equals(selfPlayer.getOpponent())) {
                numberOfTurns++;
                if (numberOfTurns == 3) {
                    selfPlayer.forceRemoveCardFromField(selfCard);
                    isActivated = false;
                }
            }
        }
        return true;
    }

    public void consider(Event event) {

    }
}

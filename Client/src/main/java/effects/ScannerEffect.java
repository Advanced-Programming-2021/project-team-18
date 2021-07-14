package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import events.TurnChangeEvent;
import view.UtilityView;

public class ScannerEffect extends Effect {
    private int placeOnField;
    private boolean isActivated = false;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        return true;
    }

    private MonsterCard handleEffectIO() {
        Card selectedCard = selfPlayer.getOpponent().obtainCardFromGraveYard();
        while (!(selectedCard instanceof MonsterCard)) {
            UtilityView.showError("The selected Card isn't a monster card! try again");
            selectedCard = selfPlayer.getOpponent().obtainCardFromGraveYard();
        }
        return (MonsterCard) selectedCard;
    }

    public void consider(Event event) {
        if (event instanceof CardEvent) {
            Card card = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (((info == CardEventInfo.ENTRANCE && card.isFaceUp())
                    || info == CardEventInfo.FLIP)
                    && card.hasEffect(this)
                    && selfCard == null) {
                selfCard = card;
                isActivated = true;
                placeOnField = card.getPlayer().getMonsterPositionOnBoard((MonsterCard) card);
                selfPlayer = selfCard.getPlayer();
            }
        }
        if (event instanceof TurnChangeEvent && isActivated) {
            int graveyardMonsterSize = selfCard.getPlayer().getOpponent().getNumberOfMonstersInGraveyard();
            if (graveyardMonsterSize == 0) {
                UtilityView.showError("Opponent does not have any monster cards in their graveyard, so the Scanner cannot activate its effect");
                return;
            }
            MonsterCard card = handleEffectIO();
            selfPlayer.getMonstersFieldList()[placeOnField] = (MonsterCard) card.cloneCard();
            selfCard = selfPlayer.getMonstersFieldList()[placeOnField];
            selfPlayer.getMonstersFieldList()[placeOnField].addEffect(this);
        }
    }
}

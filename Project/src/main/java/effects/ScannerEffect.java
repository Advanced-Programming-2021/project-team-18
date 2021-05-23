package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import events.TurnChangeEvent;
import game.Player;
import utility.Utility;

public class ScannerEffect extends Effect {
    private Card sourceCard;
    private int placeOnField;
    private Player player;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        return true;
    }

    private MonsterCard handleEffectIO() {
        player.getOpponent().showGraveyard();
        Card selectedCard = player.getOpponent().obtainCardFromGraveYard();
        while (!(selectedCard instanceof MonsterCard)) {
            Printer.prompt("The selected Card isn't a monster card! try again");
            player.getOpponent().showGraveyard();
            selectedCard = player.getOpponent().obtainCardFromGraveYard();
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
                    && sourceCard == null) {
                sourceCard = card;
                placeOnField = card.getPlayer().getMonsterPositionOnBoard((MonsterCard) card);
                player = sourceCard.getPlayer();
            }
        }
        if (event instanceof TurnChangeEvent) {
            int graveyardMonsterSize = sourceCard.getPlayer().getOpponent().getNumberOfMonstersInGraveyard();
            if (graveyardMonsterSize == 0) {
                Printer.prompt("Opponent does not have any monster cards in their graveyard, so the Scanner cannot activate its effect");
                return;
            }
            MonsterCard card = handleEffectIO();
            player.getMonstersFieldList()[placeOnField] = (MonsterCard) card.cloneCard();
            sourceCard = player.getMonstersFieldList()[placeOnField];
            player.getMonstersFieldList()[placeOnField].addEffect(this);
        }
    }
}

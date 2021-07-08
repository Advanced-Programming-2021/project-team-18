package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import events.SummonEvent;
import view.UtilityView;

// Terratiger, the Empowered Warrior
public class SummonDefensePositionMonsterEffect extends Effect {
    private int maximumLevelAllowed;

    public void runEffect() {
        Card card = selfPlayer.obtainCardFromHand();
        if (!(card instanceof MonsterCard) || ((MonsterCard) card).getCardLevel() > 4) {
            UtilityView.showError("invalid command!");
            return;
        }
        int placeOnField = selfPlayer.getFirstEmptyPlaceOnMonstersField();
        if (!selfPlayer.getPermissionFromAllEffects(new CardEvent(card, CardEventInfo.ENTRANCE, selfCard)) || placeOnField == -1) {
            UtilityView.showError("you can't summon this card!");
            return;
        }
        card.setFaceUp(true);
        ((MonsterCard) card).setDefenseMode(true);
        selfPlayer.getMonstersFieldList()[placeOnField] = (MonsterCard) card;
        selfPlayer.removeCardFromHand(card);
        UtilityView.displayMessage("card summoned successfully!");
    }

    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        if (event instanceof SummonEvent) {
            MonsterCard card = ((SummonEvent) event).getMonster();
            if (!((SummonEvent) event).isSpecial() && selfPlayer == null) {
                selfPlayer = card.getPlayer();
                selfCard = card;
                while (true) {
                    if (selfPlayer.obtainConfirmation("Do you want to activate Terratiger, the Empowered Warrior's effect?")) {
                        runEffect();
                    } else return;
                }
            }
        }
    }
}

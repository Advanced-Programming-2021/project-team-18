package effects;

import card.MonsterCard;
import data.Printer;
import events.Event;
import events.SpellTrapActivationEvent;
import game.Deck;
import view.UtilityView;


// By Sina
// cards having this effect: Monster Reborn
public class SummonFromGraveyardEffect extends Effect {

    private void runEffect() {
        Deck summonOrigin = null;
        if (selfPlayer.getGraveyard().isEmpty())
            summonOrigin = selfPlayer.getOpponent().getGraveyard();
        if (selfPlayer.getOpponent().getGraveyard().isEmpty()) {
            if (summonOrigin != null) {
                UtilityView.displayMessage("Sorry, both graveyards are empty!", selfPlayer.getUser());
                return;
            }
            summonOrigin = selfPlayer.getGraveyard();
        }
        if (summonOrigin == null) {
            if (selfPlayer.obtainConfirmation("Do you want to summon from your graveyard? (yes/no)"))
                summonOrigin = selfPlayer.getGraveyard();
            else summonOrigin = selfPlayer.getOpponent().getGraveyard();
        }
        MonsterCard monsterCard = selfPlayer.obtainMonsterCard(summonOrigin);
        if (monsterCard.isRitual()) {
            UtilityView.showError(selfPlayer.getUser(), "Sorry, cannot summon ritual monsters!");
            return;
        }
        if (!selfPlayer.addMonsterCardToField(monsterCard)) {
            UtilityView.showError(selfPlayer.getUser(), "Sorry, you don't have enough space for new monster cards!");
            return;
        }
        summonOrigin.removeCard(monsterCard);
        monsterCard.setFaceUp(selfPlayer.obtainConfirmation(
                "Would you like to add this card in attack position? (yes/no)"));
        destroyYourself();
    }

    private void destroyYourself() {
        selfPlayer.forceRemoveCardFromField(selfCard);
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        return true;
    }

    public void consider(Event event) {
        if (event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent partEvent = (SpellTrapActivationEvent) event;
            if (partEvent.getCard() == selfCard) runEffect();
        }
    }
}

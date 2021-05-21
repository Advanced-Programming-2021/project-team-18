package effects;

import card.Card;
import card.MonsterCard;
import card.MonsterCardType;
import data.Printer;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import events.SpellTrapActivationEvent;
import utility.Utility;

import java.util.ArrayList;

public class MagnumShieldEffect extends Effect {
    MonsterCard equippedMonster;
    int originalAttack, originalDefense;

    private ArrayList<String> getWarriors() {
        ArrayList<String> warriors = new ArrayList<>();
        for (MonsterCard monsterCard : selfPlayer.getMonstersFieldList()) {
            if (monsterCard.getMonsterType() == MonsterCardType.WARRIOR)
                warriors.add(monsterCard.getCardName());
        }
        return warriors;
    }

    private boolean runEffect() {
        ArrayList<String> warriors = getWarriors();
        if (Utility.checkAndPrompt(
                warriors.isEmpty(),
                "You don't have any warrior monsters to equip!")) return false;
        String cardName = Utility.askPlayer(
                selfPlayer, "please select a warrior monster", warriors
        );
        for (MonsterCard monsterCard : selfPlayer.getMonstersFieldList()) {
            if (monsterCard.getCardName() == cardName) {
                originalAttack = monsterCard.getCardAttack();
                originalDefense = monsterCard.getCardDefense();
                if (monsterCard.isDefenseMode())
                    monsterCard.setCardDefense(
                            originalDefense + monsterCard.getCardAttack()
                    );
                else
                    monsterCard.setCardAttack(
                            originalAttack + monsterCard.getCardDefense()
                    );
                return true;
            }
        }
        return false;
    }

    private void counterAct() {
        if (equippedMonster == null) return;
        equippedMonster.setCardAttack(originalAttack);
        equippedMonster.setCardDefense(originalDefense);
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent partEvent = (SpellTrapActivationEvent) event;
            if (partEvent.getCard() == selfCard) {
                return runEffect();
            }
        }
        if (event instanceof CardEvent) {
            CardEvent partEvent = (CardEvent) event;
            if (partEvent.getCard() == equippedMonster) {
                CardEventInfo info = partEvent.getInfo();
                if (info == CardEventInfo.DESTROYED) {
                    counterAct();
                    selfPlayer.removeCardFromField(selfCard , null);
                }
            }
        }
        return false;
    }

    public void consider(Event event) {

    }
}

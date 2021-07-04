package effects;

import card.MonsterCard;
import card.MonsterCardType;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import events.SpellTrapActivationEvent;
import utility.Utility;

import java.util.ArrayList;

// By Sina
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

    private void runEffect() {
        ArrayList<String> warriors = getWarriors();
        String cardName = Utility.askPlayer(
                selfPlayer, "please select a warrior monster", warriors
        );
        for (MonsterCard monsterCard : selfPlayer.getMonstersFieldList()) {
            if (monsterCard.getCardName().equals(cardName)) {
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
                return;
            }
        }
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
                return !Utility.checkAndPrompt(
                        getWarriors().isEmpty(),
                        "You don't have any warrior monsters to equip!");
            }
        }
        return true;
    }

    public void consider(Event event) {
        if (event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent partEvent = (SpellTrapActivationEvent) event;
            if (partEvent.getCard() == selfCard) {
                runEffect();
            }
        }
        if (event instanceof CardEvent) {
            CardEvent partEvent = (CardEvent) event;
            if (partEvent.getCard() == equippedMonster) {
                if (partEvent.getInfo() == CardEventInfo.DESTROYED) {
                    counterAct();
                    selfPlayer.removeCardFromField(selfCard, null);
                }
            }
        }
    }
}

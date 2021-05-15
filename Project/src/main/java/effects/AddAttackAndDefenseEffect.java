package effects;


import card.Card;
import card.MonsterCard;
import card.MonsterCardType;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;

import java.rmi.server.RMISocketFactory;

// by Pasha
// Note : every time a card with this effect gets summoned it should also call its own permit with its own event
// cards with this effect : [command knight , Yami , Forest , Closed Forest , UMIRUKA]
public class AddAttackAndDefenseEffect extends Effect {
    private int attackAddValue;
    private int defenseAddValue;
    private MonsterCardType monsterType;
    private int attackAddedPerGraveyardMonsters;

    private void toggleSelfEffect(int coefficient) {
        for (int i = 1; i <= Player.FIELD_SIZE; ++i) {
            MonsterCard monsterCard = selfPlayer.getMonstersFieldList()[i];
            if (monsterCard != null && (monsterCard.getMonsterType() == monsterType) || monsterType == MonsterCardType.ALL) {
                monsterCard.setCardAttack(monsterCard.getCardAttack() + (attackAddValue * coefficient));
                monsterCard.setCardDefense(monsterCard.getCardDefense() + (defenseAddValue * coefficient));
                monsterCard.setCardAttack(monsterCard.getCardAttack() + (coefficient * attackAddedPerGraveyardMonsters * selfPlayer.getGraveyard().getCardsList().size()));
            }
        }
    }

    private void whenPlayedEffect() {
        toggleSelfEffect(1);
    }

    private void whenDestroyedEffect() {
        toggleSelfEffect(-1);
    }

    private void whenOtherMonsterCardPlayedEffect(MonsterCard monsterCard) {
        if (monsterCard != null && (monsterCard.getMonsterType() == monsterType) || monsterType == MonsterCardType.ALL) {
            monsterCard.setCardAttack(monsterCard.getCardAttack() + (attackAddValue));
            monsterCard.setCardDefense(monsterCard.getCardDefense() + (defenseAddValue));
            monsterCard.setCardAttack(monsterCard.getCardAttack() + (attackAddedPerGraveyardMonsters * selfPlayer.getGraveyard().getCardsList().size()));
        }
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if (((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || (cardEventInfo == CardEventInfo.FLIP)) && card.hasEffect(this)) {
                whenPlayedEffect();
            } else if ((cardEventInfo == CardEventInfo.DESTROYED) && card.hasEffect(this)) {
                whenDestroyedEffect();
            } else if (((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || (cardEventInfo == CardEventInfo.FLIP)) && card.getPlayer() == selfPlayer && card instanceof MonsterCard) {
                whenOtherMonsterCardPlayedEffect((MonsterCard) card);
            }
        }
        return true;
    }
}
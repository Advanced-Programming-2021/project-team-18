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
// Note : every time a card with this effect gets summoned it should also called its own permit with its own event
public class AddAttackAndDefenseEffect extends Effect {
    private int attackAddValue;
    private int defenseAddValue;
    private MonsterCardType monsterType;
    private int attackAddedPerGraveyardMonsters;
    private Player player;

    private void toggleSelfEffect(int coefficient) {
        for (int i = 1; i <= Player.FIELD_SIZE; ++i) {
            MonsterCard monsterCard = player.getMonstersFieldList()[i];
            if (monsterCard != null && (monsterCard.getMonsterType() == monsterType) || monsterType == MonsterCardType.ALL) {
                monsterCard.setCardAttack(monsterCard.getCardAttack() + (attackAddValue * coefficient));
                monsterCard.setCardDefense(monsterCard.getCardDefense() + (defenseAddValue * coefficient));
                monsterCard.setCardAttack(monsterCard.getCardAttack() + (coefficient * attackAddedPerGraveyardMonsters * player.getGraveyard().getCardsList().size()));
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
            monsterCard.setCardAttack(monsterCard.getCardAttack() + (attackAddedPerGraveyardMonsters * player.getGraveyard().getCardsList().size()));
        }
    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if (cardEventInfo == CardEventInfo.ENTRANCE && card.hasEffect(this))
                player = card.getPlayer();
            if (((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || (cardEventInfo == CardEventInfo.FLIP)) && card.hasEffect(this)) {
                whenPlayedEffect();
            } else if ((cardEventInfo == CardEventInfo.DESTROYED) && card.hasEffect(this)) {
                whenDestroyedEffect();
            } else if (((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || (cardEventInfo == CardEventInfo.FLIP)) && card.getPlayer() == player && card instanceof MonsterCard) {
                whenOtherMonsterCardPlayedEffect((MonsterCard) card);
            }
        }
        return true;
    }
}
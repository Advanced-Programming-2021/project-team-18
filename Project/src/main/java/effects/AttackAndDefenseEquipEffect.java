package effects;

import card.Card;
import card.MonsterCard;
import card.MonsterCardType;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;
// by Pasha
// cards with this effect : [sword of dark destruction , black pendant]
public class AttackAndDefenseEquipEffect extends Effect {
    private int addAttack;
    private int addDefense;
    private MonsterCardType monsterCardType;
    public AttackAndDefenseEquipEffect(int addAttack , int addDefense , MonsterCardType monsterCardType) {
        this.addAttack = addAttack;
        this.addDefense = addDefense;
        this.monsterCardType = monsterCardType;
    }
    private int spellPosition;

    private void toggleSelfEffect(int coefficient) {
        MonsterCard monsterCard = selfPlayer.getMonstersFieldList()[spellPosition];
        if(monsterCard == null || (monsterCard.getMonsterType() != monsterCardType && monsterCardType != MonsterCardType.ALL)) return ;
        monsterCard.setCardAttack(monsterCard.getCardAttack() + addAttack * coefficient);
        monsterCard.setCardDefense(monsterCard.getCardDefense() + addDefense * coefficient);
    }
    private void activateEffect() { toggleSelfEffect(1); }
    private void deactivateEffect() { toggleSelfEffect(-1); }
    private void checkIfMonsterDestroyed() {
        if(selfPlayer.getMonstersFieldList()[spellPosition] == null) {
            selfPlayer.removeCardFromField(selfCard , null);
        }
    }
    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        spellPosition = selfPlayer.getSpellOrTrapPositionOnBoard(selfCard);
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if(((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || cardEventInfo == CardEventInfo.FLIP) && card.hasEffect(this)) {
                activateEffect();
            } else if(card.hasEffect(this) && cardEventInfo == CardEventInfo.DESTROYED) {
                deactivateEffect();
            } else if(((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || cardEventInfo == CardEventInfo.FLIP) && card instanceof MonsterCard && selfPlayer.getMonsterPositionOnBoard((MonsterCard) card) == spellPosition) {
                activateEffect();
            }
        }
        checkIfMonsterDestroyed();
        isInConsideration = false;
    }
}

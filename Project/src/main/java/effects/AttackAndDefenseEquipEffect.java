package effects;

import card.Card;
import card.MonsterCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;
// by Pasha
// cards with this effect : [sword of dark destruction , black pendant]
public class AttackAndDefenseEquipEffect extends Effect {
    private int addAttack;
    private int addDefense;
    private Player player;
    private int spellPosition;

    private void toggleSelfEffect(int coefficient) {
        MonsterCard monsterCard = player.getMonstersFieldList()[spellPosition];
        if(monsterCard == null) return ;
        monsterCard.setCardAttack(monsterCard.getCardAttack() + addAttack * coefficient);
        monsterCard.setCardDefense(monsterCard.getCardDefense() + addDefense * coefficient);
    }
    private void activateEffect() { toggleSelfEffect(1); }
    private void deactivateEffect() { toggleSelfEffect(-1); }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if (cardEventInfo == CardEventInfo.ENTRANCE && card.hasEffect(this)) {
                player = card.getPlayer();
                spellPosition = player.getSpellOrTrapPositionOnBoard(card);
            }
            if(((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || cardEventInfo == CardEventInfo.FLIP) && card.hasEffect(this)) {
                activateEffect();
            } else if(card.hasEffect(this) && cardEventInfo == CardEventInfo.DESTROYED) {
                deactivateEffect();
            } else if(((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || cardEventInfo == CardEventInfo.FLIP) && card instanceof MonsterCard && player.getMonsterPositionOnBoard((MonsterCard) card) == spellPosition) {
                activateEffect();
            }
        }
        return true;
    }
}

package effects;

import card.Card;
import card.MonsterCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;
// by Pasha
// cards with this effect : [calculator]
public class CalculatorAttackEffect extends Effect{
    private int attackPerLevel;
    private int currentAttackAdded;
    private void calculateCurrentAttack() {
        int previousAttackAdded = currentAttackAdded;
        currentAttackAdded = 0;
        for(int i = 1;i <= Player.FIELD_SIZE;++ i) {
            MonsterCard monsterCard = selfPlayer.getMonstersFieldList()[i];
            if(monsterCard != null && monsterCard.isFaceUp()) {
                currentAttackAdded += monsterCard.getCardLevel() * attackPerLevel;
            }
         }
        int differenceAttack = currentAttackAdded - previousAttackAdded;
        ((MonsterCard)selfCard).setCardAttack(((MonsterCard)selfCard).getCardAttack() + differenceAttack);
    }
    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        calculateCurrentAttack();
        return true;
    }
}

package effects;

import card.MonsterCard;
import events.Event;
import game.Player;

// by Pasha
// cards with this effect : [calculator]
public class CalculatorAttackEffect extends Effect {
    private int attackPerLevel;
    private int currentAttackAdded;

    public CalculatorAttackEffect(int attackPerLevel) {
        this.attackPerLevel = attackPerLevel;
    }

    private void calculateCurrentAttack() {
        int previousAttackAdded = currentAttackAdded;
        currentAttackAdded = 0;
        for (int i = 1; i <= Player.getFIELD_SIZE(); ++i) {
            MonsterCard monsterCard = selfPlayer.getMonstersFieldList()[i];
            if (monsterCard != null && monsterCard.isFaceUp()) {
                currentAttackAdded += monsterCard.getCardLevel() * attackPerLevel;
            }
        }
        int differenceAttack = currentAttackAdded - previousAttackAdded;
        ((MonsterCard) selfCard).setCardAttack(((MonsterCard) selfCard).getCardAttack() + differenceAttack);
    }

    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        calculateCurrentAttack();
        isInConsideration = false;
    }
}

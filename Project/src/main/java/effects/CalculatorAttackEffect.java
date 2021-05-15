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
    private Player player;
    private MonsterCard card;
    private void calculateCurrentAttack() {
        int previousAttackAdded = currentAttackAdded;
        currentAttackAdded = 0;
        for(int i = 1;i <= Player.FIELD_SIZE;++ i) {
            MonsterCard monsterCard = player.getMonstersFieldList()[i];
            if(monsterCard != null && monsterCard.isFaceUp()) {
                currentAttackAdded += monsterCard.getCardLevel() * attackPerLevel;
            }
         }
        int differenceAttack = currentAttackAdded - previousAttackAdded;
        card.setCardAttack(card.getCardAttack() + differenceAttack);
    }
    public boolean permit(Event event) {
        if(event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if(card.hasEffect(this)) {
                this.card = (MonsterCard) card;
                player = card.getPlayer();
            }
        }
        calculateCurrentAttack();
        return true;
    }
}

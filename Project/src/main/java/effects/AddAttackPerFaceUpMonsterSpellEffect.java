package effects;

import card.Card;
import card.MonsterCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;



public class AddAttackPerFaceUpMonsterSpellEffect extends Effect {
    private int attackAmount;
    Player player;
    int positionOnBoard = -1;

    private void toggleSelfEffect(int coefficient) {
        int faceUpCount = 0;
        for(int i = 1;i <= Player.FIELD_SIZE;++ i) {
            MonsterCard monsterCard = player.getMonstersFieldList()[i];
            if(monsterCard != null && monsterCard.isFaceUp())
                ++ faceUpCount;
        }
        MonsterCard monsterCard = player.getMonstersFieldList()[positionOnBoard];
        if(monsterCard != null) {
           monsterCard.setCardAttack(monsterCard.getCardAttack() + coefficient * attackAmount * faceUpCount);
        }
    }

    private void whenPlayedEffect() {
        toggleSelfEffect(1);
    }

    private void whenDestroyedEffect() {
        toggleSelfEffect(-1);
    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if (cardEventInfo == CardEventInfo.ENTRANCE && card.hasEffect(this)) {
                player = card.getPlayer();
                positionOnBoard = player.getSpellOrTrapPositionOnBoard(card);
            }
            if (card.hasEffect(this) && ((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || cardEventInfo == CardEventInfo.FLIP)) {
                whenPlayedEffect();
            } else if ((cardEventInfo == CardEventInfo.DESTROYED) && card.hasEffect(this)) {
                whenDestroyedEffect();
            }

        }
        return true;
    }
}

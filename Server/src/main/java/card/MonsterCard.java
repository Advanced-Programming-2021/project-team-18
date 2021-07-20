package card;

import com.google.gson.annotations.Expose;
import data.Printer;
import effects.Effect;
import game.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class MonsterCard extends Card {
    private MonsterCardAttribute cardAttribute;
    private int cardLevel;
    private MonsterCardType monsterType;
    private int cardAttack;
    private int cardDefense;
    @Expose
    private boolean isDefenseMode;
    @Expose
    private boolean hasAttackedThisTurn = false;
    @Expose
    private boolean hasChangedPositionThisTurn = false;
    private String cardType;
    public MonsterCard() {

    }
    public MonsterCard(String cardName, int price, String cardNumber, String cardDescription, boolean isFaceUp, Player player, Origin cardOrigin, ArrayList<Effect> effects) {
        super(cardName, price, cardNumber, cardDescription, isFaceUp, player, cardOrigin, effects);
    }

    public boolean isRitual() {
        return cardType.equals("Ritual");
    }

    public void attackTo(MonsterCard attackedMonster, Player owner) {
        if (hasAttackedThisTurn) return;
        if (attackedMonster.isDefenseMode()) {
            owner.getOpponent().flipMonsterOnDefense(attackedMonster, this);
            if (this.getCardAttack() == attackedMonster.getCardDefense()) {
                Printer.prompt(owner , "no card is destroyed");
            } else if (this.getCardAttack() > attackedMonster.getCardDefense()) {
                Printer.prompt(owner , "the defense position monster is destroyed");
                owner.getOpponent().removeCardFromField(attackedMonster, this);
            } else if (this.getCardAttack() < attackedMonster.getCardDefense()) {
                Printer.prompt(owner , "no card is destroyed and you received " + (attackedMonster.getCardDefense() - this.getCardAttack()) + " battle damage");
                owner.decreaseLifePoint(attackedMonster.getCardDefense() - this.getCardAttack(), attackedMonster);
            }
        } else {
            if (this.getCardAttack() == attackedMonster.getCardAttack()) {
                Printer.prompt(owner , "both you and your opponent monster cards are destroyed and no one receives damage");
                owner.removeCardFromField(this, attackedMonster);
                owner.getOpponent().removeCardFromField(attackedMonster, this);
            } else if (this.getCardAttack() > attackedMonster.getCardAttack()) {
                Printer.prompt(owner , "your opponent’s monster is destroyed and your opponent receives " + (this.getCardAttack() - attackedMonster.getCardAttack()) + " battle damage");
                owner.getOpponent().removeCardFromField(attackedMonster, this);
                owner.getOpponent().decreaseLifePoint(this.getCardAttack() - attackedMonster.getCardAttack(), this);
            } else if (this.getCardAttack() < attackedMonster.getCardAttack()) {
                Printer.prompt(owner , "Your monster card is destroyed and you received " + (attackedMonster.getCardAttack() - this.getCardAttack()) + " battle damage");
                owner.decreaseLifePoint(attackedMonster.getCardAttack() - this.getCardAttack(), attackedMonster);
                owner.removeCardFromField(this, attackedMonster);
            }
        }
        hasAttackedThisTurn = true;
    }

    @Override
    public void showCard() {

    }

    @Override
    public Card cloneCard() {
        MonsterCard card = new MonsterCard();
        this.cloneDefaults(card);
        card.setCardAttribute(this.getCardAttribute());
        card.setCardLevel(this.getCardLevel());
        card.setMonsterType(this.getMonsterType());
        card.setCardAttack(this.getCardAttack());
        card.setCardDefense(this.getCardDefense());
        card.setCardType(this.getCardType());
        return card;
    }

}

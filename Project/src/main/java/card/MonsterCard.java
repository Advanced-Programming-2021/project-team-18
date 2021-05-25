package card;

import data.Printer;
import game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonsterCard extends Card {
    private MonsterCardAttribute cardAttribute;
    private int cardLevel;
    private MonsterCardType monsterType;
    private int cardAttack;
    private int cardDefense;
    private boolean isDefenseMode;
    private boolean hasAttackedThisTurn = false;
    private boolean hasChangedPositionThisTurn = false;
    private String cardType;

    public boolean isRitual() {
        return cardType.equals("Ritual");
    }

    public void attackTo(MonsterCard attackedMonster, Player owner) {
        if (attackedMonster.isDefenseMode()) {
            owner.getOpponent().flipMonsterOnDefense(attackedMonster, this);
            if (this.getCardAttack() == attackedMonster.getCardDefense()) {
                Printer.prompt("no card is destroyed");
            } else if (this.getCardAttack() > attackedMonster.getCardDefense()) {
                Printer.prompt("the defense position monster is destroyed");
                owner.getOpponent().removeCardFromField(attackedMonster, this);
            } else if (this.getCardAttack() < attackedMonster.getCardDefense()) {
                Printer.prompt("no card is destroyed and you received " + (attackedMonster.getCardDefense() - this.getCardAttack()) + " battle damage");
                owner.decreaseLifePoint(attackedMonster.getCardDefense() - this.getCardAttack(), attackedMonster);
            }
        } else {
            if (this.getCardAttack() == attackedMonster.getCardAttack()) {
                Printer.prompt("both you and your opponent monster cards are destroyed and no one receives damage");
                owner.removeCardFromField(this, attackedMonster);
                owner.getOpponent().removeCardFromField(attackedMonster, this);
            } else if (this.getCardAttack() > attackedMonster.getCardAttack()) {
                Printer.prompt("your opponent’s monster is destroyed and your opponent receives " + (this.getCardAttack() - attackedMonster.getCardAttack()) + " battle damage");
                owner.getOpponent().removeCardFromField(attackedMonster, this);
                owner.getOpponent().decreaseLifePoint(this.getCardAttack() - attackedMonster.getCardAttack(), this);
            } else if (this.getCardAttack() < attackedMonster.getCardAttack()) {
                Printer.prompt("Your monster card is destroyed and you received " + (attackedMonster.getCardAttack() - this.getCardAttack()) + " battle damage");
                owner.decreaseLifePoint(attackedMonster.getCardAttack() - this.getCardAttack(), attackedMonster);
                owner.removeCardFromField(this, attackedMonster);
            }
        }
    }

    @Override
    public void showCard() {
        String result = "Name: " + this.getCardName() + "\n";
        result += "Level: " + this.getCardLevel() + "\n";
        result += "Type: " + this.getMonsterType() + "\n";
        result += "ATK: " + this.getCardAttack() + "\n";
        result += "DEF: " + this.getCardDefense() + "\n";
        result += "Description " + this.getCardDescription() + "\n";
        Printer.prompt(result);
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
        card.setEffects(this.getEffects());
        return card;
    }

}

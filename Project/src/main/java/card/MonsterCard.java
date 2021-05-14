package card;

import data.Printer;
import effects.Effect;
import events.Event;

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
    private boolean isDefenseMode;
    private boolean hasAttackedThisTurn = false;
    private String cardType;



    public void attackTo(MonsterCard attackedMonster , Player owner) {
        if(attackedMonster.isDefenseMode()) {
            if(this.getCardAttack() == attackedMonster.getCardDefense()) {
                Printer.prompt("no card is destroyed");
            } else if(this.getCardAttack() > attackedMonster.getCardDefense()) {
                Printer.prompt("the defense position monster is destroyed");
                owner.getOpponent().destroyMonster(attackedMonster);
            } else if(this.getCardAttack() < attackedMonster.getCardDefense()) {
                Printer.prompt("no card is destroyed and you received " + (attackedMonster.getCardDefense() - this.getCardAttack()) + " battle damage");
                owner.setLifePoint(owner.getLifePoint() - attackedMonster.getCardDefense() + this.getCardAttack());
            }
        } else {
            if(this.getCardAttack() == attackedMonster.getCardAttack()) {
                Printer.prompt("both you and your opponent monster cards are destroyed and no one receives damage");
                owner.destroyMonster(this);
                owner.getOpponent().destroyMonster(attackedMonster);
            } else if(this.getCardAttack() > attackedMonster.getCardAttack()) {
                Printer.prompt("your opponent’s monster is destroyed and your opponent receives " + (this.getCardAttack() - attackedMonster.getCardAttack()) + " battle damage");
                owner.getOpponent().destroyMonster(attackedMonster);
            } else if(this.getCardAttack() < attackedMonster.getCardAttack()) {
                Printer.prompt("Your monster card is destroyed and you received " + (attackedMonster.getCardAttack() - this.getCardAttack()) + " battle damage");
                owner.destroyMonster(this);
            }
        }
    }

    @Override
    public void runEffects(Event event) {

    }

    @Override
    public void showCard() {
        String result = "Name: " + this.getCardName() + "\n";
        result += "Level: " + this.getCardLevel() + "\n";
        result += "Type: " + this.getMonsterType() + "\n";
        result += "ATK: " + this.getCardAttack() + "\n";
        result += "DEF: "+ this.getCardDefense() + "\n";
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

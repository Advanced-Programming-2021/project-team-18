package card;

import data.Printer;
import effects.Effect;
import events.Event;

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
    private ArrayList<Effect> onDestroyEffects;
    private ArrayList<Effect> onAttackEffects;
    private ArrayList<Effect> onFlipEffects;
    private ArrayList<Effect> onDefenseEffects;
    private ArrayList<Effect> onSummonEffects;
    private ArrayList<Effect> onStartPhaseEffects;
    private ArrayList<Effect> onEndPhaseEffects;


    public void attackTo(MonsterCard attackedMonster) {

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
        card.setOnDestroyEffects(this.getOnDestroyEffects());
        card.setOnAttackEffects(this.getOnAttackEffects());
        card.setOnFlipEffects(this.getOnFlipEffects());
        card.setOnDefenseEffects(this.getOnDefenseEffects());
        card.setOnSummonEffects(this.getOnSummonEffects());
        card.setOnStartPhaseEffects(this.getOnStartPhaseEffects());
        card.setOnEndPhaseEffects(this.getOnEndPhaseEffects());
        return card;
    }

}

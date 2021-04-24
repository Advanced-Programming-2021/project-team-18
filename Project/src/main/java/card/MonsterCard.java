package card;

import data.Printer;
import effects.Effect;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class MonsterCard extends Card {
    private MonsterCardAttribute cardAttribute;
    private int cardLevel;
    private MonsterCardType cardType;
    private int cardAttack;
    private int cardDefense;
    private boolean isDefenseMode;
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
    public void showCard() {
        String result = "Name: " + this.getCardName() + "\n";
        result += "Level: " + this.getCardLevel() + "\n";
        result += "Type: " + this.getCardType() + "\n";
        result += "ATK: " + this.getCardAttack() + "\n";
        result += "DEF: "+ this.getCardDefense() + "\n";
        result += "Description " + this.getCardDescription() + "\n";
        Printer.prompt(result);
    }
}

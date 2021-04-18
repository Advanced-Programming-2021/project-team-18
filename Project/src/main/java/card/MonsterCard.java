package card;

import effects.Effect;

import java.util.ArrayList;

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
}

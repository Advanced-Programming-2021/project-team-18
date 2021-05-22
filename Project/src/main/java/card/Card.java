package card;

import effects.*;
import game.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter

public abstract class Card implements Comparable<Card> {
    private static final ArrayList<Card> allCards = new ArrayList<>();
    private static final ArrayList<String> allCardNames = new ArrayList<>();
    private String cardName;
    @Setter
    private int price;
    @Setter
    private String cardNumber;
    @Setter
    private String cardDescription;
    @Setter
    private boolean isFaceUp = true;
    @Setter
    private Player player;
    @Setter
    private Origin cardOrigin;
    @Setter
    private ArrayList<Effect> effects;

    public void setCardName(String cardName) {
        this.cardName = cardName;
        allCardNames.add(cardName);
    }

    public static Card getCardByName(String cardName) {
        for (Card card : allCards)
            if (card.getCardName().equals(cardName))
                return card;
        return null;
    }

    public static List<String> getAllCardNames() {
        return Collections.unmodifiableList(allCardNames);
    }

    public static ArrayList<Card> getAllCards() {
        return allCards;
    }

    public boolean hasEffect(Effect effect) {
        return effects.contains(effect);
    }

    public void cloneDefaults(Card card) {
        card.setPrice(this.getPrice());
        card.setCardName(this.getCardName());
        card.setCardNumber(this.getCardNumber());
        card.setCardDescription(this.getCardDescription());
        card.setEffects(effects);
    }

    public abstract Card cloneCard();

    public abstract void showCard();

    private void manageMonsterEffects() {

        if (cardName.equals("Command Knight")) {
            effects.add(new AddAttackAndDefenseEffect(400, 400, MonsterCardType.ALL, 0));
            // add disable attacker effect
        } else if (cardName.equals("Yomi Ship")) {
            effects.add(new DestroyAttackerEffect());
        } else if (cardName.equals("Suijin")) {
            effects.add(new SuijinEffect());
        } else if (cardName.equals("Crab Turtle")) {
            effects.add(new RitualSummonEffect());
        } else if (cardName.equals("Skull Guardian")) {
            effects.add(new RitualSummonEffect());
        } else if (cardName.equals("Man-Eater Bug")) {
            effects.add(new ManEaterBugEffect());
        } else if (cardName.equals("Gate Guardian")) {
            // not written yet
        } else if (cardName.equals("Scanner")) {
            // not written yet
        } else if (cardName.equals("Marshmallon")) {
            effects.add(new MarshmallonEffect());
        } else if (cardName.equals("Beast King Barbaros")) {
            // not written yet
        } else if (cardName.equals("Texchanger")) {
            // not written yet
        } else if (cardName.equals("The Calculator")) {
            effects.add(new CalculatorAttackEffect(300));
        } else if (cardName.equals("Mirage Dragon")) {
            effects.add(new TrapActivationDenialEffect());
        } else if (cardName.equals("Herald of Creation")) {
            // not written yet
        } else if (cardName.equals("Exploder Dragon")) {
            effects.add(new DenyLifePointChangeEffect());
            effects.add(new DestroyAttackerEffect());
        } else if (cardName.equals("Terratiger the Empowered Warrior")) {
            // not written yet
        } else if (cardName.equals("The Tricky")) {
            // not written yet
        }
    }

    private void manageSpellAndTrapEffects() {
        if (cardName.equals("Monster Reborn")) {

        } else if (cardName.equals("Terraforming")) {

        } else if (cardName.equals("Pot of Greed")) {

        } else if (cardName.equals("Raigeki")) {
            effects.add(new DestroyEnemyMonsterEffect(false));
        } else if (cardName.equals("Change of Heart")) {

        } else if (cardName.equals("Harpie's Feather Duster")) {
            effects.add(new HarpiesFeatherEffect());
        } else if (cardName.equals("Swords of Revealing Light")) {

        } else if (cardName.equals("Dark Hole")) {
            effects.add(new DestroyEnemyMonsterEffect(true));
        } else if (cardName.equals("Supply Squad")) {

        } else if (cardName.equals("Spell Absorption")) {

        } else if (cardName.equals("Messenger of peace")) {
            effects.add(new MessengerOfPeaceEffect());
        } else if (cardName.equals("Twin Twisters")) {

        } else if (cardName.equals("Mystical space typhoon")) {

        } else if (cardName.equals("Ring of defense")) {

        } else if (cardName.equals("Yami")) {
            effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.FIEND, 0));
            effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.SPELLCASTER, 0));
            effects.add(new AddAttackAndDefenseEffect(-200, -200, MonsterCardType.FAIRY, 0));
        } else if (cardName.equals("Forest")) {
            effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.INSECT, 0));
            effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.BEAST, 0));
            effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.BEASTWARRIOR, 0));
        } else if (cardName.equals("Closed Forest")) {
            effects.add(new AddAttackAndDefenseEffect(0, 0, MonsterCardType.BEAST, 100));
        } else if (cardName.equals("Umiiruka")) {
            effects.add(new AddAttackAndDefenseEffect(500, -400, MonsterCardType.AQUA, 0));
        } else if (cardName.equals("Sword of dark destruction")) {
            effects.add(new AttackAndDefenseEquipEffect(400, -200, MonsterCardType.FIEND));
            effects.add(new AttackAndDefenseEquipEffect(400, -200, MonsterCardType.SPELLCASTER));
        } else if (cardName.equals("Black Pendant")) {
            effects.add(new AttackAndDefenseEquipEffect(500, 0, MonsterCardType.ALL));
        } else if (cardName.equals("United We Stand")) {
            effects.add(new AddAttackPerFaceUpMonsterSpellEffect(800, 800));
        } else if (cardName.equals("Magnum Shield")) {
            effects.add(new MagnumShieldEffect());
        } else if (cardName.equals("Advanced Ritual Art")) {

        } else if (cardName.equals("Magic Cylinder")) {
            effects.add(new MagicCylinderEffect());
        } else if (cardName.equals("Mirror Force")) {

        } else if (cardName.equals("Mind Crush")) {
            effects.add(new MindCrushEffect());
        } else if (cardName.equals("Trap Hole")) {

        } else if (cardName.equals("Torrential Tribute")) {

        } else if (cardName.equals("Time Seal")) {

        } else if (cardName.equals("Negate Attack")) {

        } else if (cardName.equals("Solemn Warning")) {
            effects.add(new SolemnWarningEffect());
        } else if (cardName.equals("Magic Jammer")) {
            effects.add(new MagicJammerEffect());
        }
    }

    public void manageEffect() {
        // NOTE : should be called when creating players not at the beginning
        manageMonsterEffects();
        manageSpellAndTrapEffects();

    }

    public void addEffect(Effect effect){
        effects.add(effect);
    }

    @Override
    public int compareTo(Card o) {
        return this.cardName.compareTo(o.cardName);
    }
}
package card;

import effects.*;
import events.Event;
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

    public static Card createNewCard(String cardName) {
        return getCardByName(cardName).cloneCard();
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
        String name = this.getCardName();
        if (name.equals("Command Knight")) {
            effects.add(new AddAttackAndDefenseEffect(400, 400, MonsterCardType.ALL, 0));
            // add disable attacker effect
        } else if (name.equals("Yomi Ship")) {
            effects.add(new DestroyAttackerEffect());
        } else if (name.equals("Suijin")) {
            effects.add(new SuijinEffect());
        } else if (name.equals("Crab Turtle")) {
            effects.add(new RitualSummonEffect());
        } else if (name.equals("Skull Guardian")) {
            effects.add(new RitualSummonEffect());
        } else if (name.equals("Man-Eater Bug")) {
            effects.add(new ManEaterBugEffect());
        } else if (name.equals("Gate Guardian")) {
            // not written yet
        } else if (name.equals("Scanner")) {
            // not written yet
        } else if (name.equals("Marshmallon")) {
            effects.add(new MarshmallonEffect());
        } else if (name.equals("Beast King Barbaros")) {
            // not written yet
        } else if (name.equals("Texchanger")) {
            // not written yet
        } else if (name.equals("The Calculator")) {
            effects.add(new CalculatorAttackEffect(300));
        } else if (name.equals("Mirage Dragon")) {
            effects.add(new TrapActivationDenialEffect());
        } else if (name.equals("Herald of Creation")) {
            // not written yet
        } else if (name.equals("Exploder Dragon")) {
            effects.add(new DenyLifePointChangeEffect());
            effects.add(new DestroyAttackerEffect());
        } else if (name.equals("Terratiger the Empowered Warrior")) {
            // not written yet
        } else if (name.equals("The Tricky")) {
            // not written yet
        } else if (name.equals("Harpie's Feather Duster")) {
            effects.add(new HarpiesFeatherEffect());
        } else if (name.equals("Magic Jammer")) {
            effects.add(new MagicJammerEffect());
        } else if (name.equals("Magnum Shield")) {
            effects.add(new MagnumShieldEffect());
        } else if (name.equals("Magic Cylinder")) {
            effects.add(new MagicCylinderEffect());
        } else if (name.equals("Messenger of peace")) {
            effects.add(new MessengerOfPeaceEffect());
        } else if (name.equals("Mind Crush")) {
            effects.add(new MindCrushEffect());
        } else if (name.equals("Solemn Warning")) {
            effects.add(new SolemnWarningEffect());
        }
    }

    private void manageSpellAndTrapEffects() {

    }

    public void manageEffect() {
        // NOTE : should be called when creating players not at the beginning
        manageMonsterEffects();
        manageSpellAndTrapEffects();
    }

    @Override
    public int compareTo(Card o) {
        return this.cardName.compareTo(o.cardName);
    }
}
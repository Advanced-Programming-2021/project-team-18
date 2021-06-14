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
        if (!allCardNames.contains(cardName))
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

        switch (cardName) {
            case "Command Knight":
                effects.add(new AddAttackAndDefenseEffect(400, 400, MonsterCardType.ALL, 0));
                // add disable attacker effect
                break;
            case "Yomi Ship":
                effects.add(new DestroyAttackerEffect());
                break;
            case "Suijin":
                effects.add(new SuijinEffect());
                break;
            case "Crab Turtle":
            case "Skull Guardian":
                effects.add(new RitualSummonEffect());
                break;
            case "Man-Eater Bug":
                effects.add(new ManEaterBugEffect());
                break;
            case "Gate Guardian":
                // not written yet
                break;
            case "Scanner":
                // not written yet
                break;
            case "Marshmallon":
                effects.add(new MarshmallonEffect());
                break;
            case "Beast King Barbaros":
                // not written yet
                break;
            case "Texchanger":
                // not written yet
                break;
            case "The Calculator":
                effects.add(new CalculatorAttackEffect(300));
                break;
            case "Mirage Dragon":
                effects.add(new TrapActivationDenialEffect());
                break;
            case "Herald of Creation":
                // not written yet
                break;
            case "Exploder Dragon":
                effects.add(new DenyLifePointChangeEffect());
                effects.add(new DestroyAttackerEffect());
                break;
            case "Terratiger the Empowered Warrior":
                // not written yet
                break;
            case "The Tricky":
                // not written yet
                break;
        }
    }

    private void manageSpellAndTrapEffects() {
        switch (cardName) {
            case "Monster Reborn":

                break;
            case "Terraforming":

                break;
            case "Pot of Greed":

                break;
            case "Raigeki":
                effects.add(new DestroyEnemyMonsterEffect(false));
                break;
            case "Change of Heart":

                break;
            case "Harpie's Feather Duster":
                effects.add(new HarpiesFeatherEffect());
                break;
            case "Swords of Revealing Light":

                break;
            case "Dark Hole":
                effects.add(new DestroyEnemyMonsterEffect(true));
                break;
            case "Supply Squad":

                break;
            case "Spell Absorption":

                break;
            case "Messenger of peace":
                effects.add(new MessengerOfPeaceEffect());
                break;
            case "Twin Twisters":

                break;
            case "Mystical space typhoon":

                break;
            case "Ring of defense":

                break;
            case "Yami":
                effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.FIEND, 0));
                effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.SPELLCASTER, 0));
                effects.add(new AddAttackAndDefenseEffect(-200, -200, MonsterCardType.FAIRY, 0));
                break;
            case "Forest":
                effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.INSECT, 0));
                effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.BEAST, 0));
                effects.add(new AddAttackAndDefenseEffect(200, 200, MonsterCardType.BEASTWARRIOR, 0));
                break;
            case "Closed Forest":
                effects.add(new AddAttackAndDefenseEffect(0, 0, MonsterCardType.BEAST, 100));
                break;
            case "Umiiruka":
                effects.add(new AddAttackAndDefenseEffect(500, -400, MonsterCardType.AQUA, 0));
                break;
            case "Sword of dark destruction":
                effects.add(new AttackAndDefenseEquipEffect(400, -200, MonsterCardType.FIEND));
                effects.add(new AttackAndDefenseEquipEffect(400, -200, MonsterCardType.SPELLCASTER));
                break;
            case "Black Pendant":
                effects.add(new AttackAndDefenseEquipEffect(500, 0, MonsterCardType.ALL));
                break;
            case "United We Stand":
                effects.add(new AddAttackPerFaceUpMonsterSpellEffect(800, 800));
                break;
            case "Magnum Shield":
                effects.add(new MagnumShieldEffect());
                break;
            case "Advanced Ritual Art":

                break;
            case "Magic Cylinder":
                effects.add(new MagicCylinderEffect());
                break;
            case "Mirror Force":

                break;
            case "Mind Crush":
                effects.add(new MindCrushEffect());
                break;
            case "Trap Hole":

                break;
            case "Torrential Tribute":

                break;
            case "Time Seal":

                break;
            case "Negate Attack":

                break;
            case "Solemn Warning":
                effects.add(new SolemnWarningEffect());
                break;
            case "Magic Jammer":
                effects.add(new MagicJammerEffect());
                break;
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
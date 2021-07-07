package card;

import effects.*;
import game.Player;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Getter

public abstract class Card implements Comparable<Card> {
    private static final ArrayList<Card> allCards = new ArrayList<>();
    private static final ArrayList<String> allCardNames = new ArrayList<>();
    private static final HashMap<String, Image> cardImages = new HashMap<>();
    private String cardName;
    @Setter
    private int price;
    @Setter
    private String cardNumber;
    @Setter
    private String cardDescription;
    @Setter
    private boolean isFaceUp = false;
    @Setter
    private Player player;
    @Setter
    private Origin cardOrigin;
    @Setter
    private ArrayList<Effect> effects = new ArrayList<>();

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

    public static ArrayList<String> getAllCardNames() {
        return allCardNames;
    }

    public static ArrayList<Card> getAllCards() {
        return allCards;
    }

    public boolean hasEffect(Effect effect) {
        return effects.contains(effect);
    }

    public Image getImage() {
        if (cardImages.get(cardName) != null)
            return cardImages.get(cardName);
        String path = "/cards_images/" + cardName.replaceAll(" ", "_") + ".jpg";
        try {
            Image image = new Image(Objects.requireNonNull(
                    getClass().getResource(path)).toExternalForm());
            cardImages.put(cardName, image);
            return image;
        } catch (Exception e) {
            if (cardImages.get("smiley") != null)
                return cardImages.get("smiley");
            Image image = new Image(Objects.requireNonNull(
                    getClass().getResource("/cards_images/Smiley.jpg")).toExternalForm());
            cardImages.put("smiley", image);
            return image;
        }
    }

    public void cloneDefaults(Card card) {
        card.setPrice(this.getPrice());
        card.setCardName(this.getCardName());
        card.setCardNumber(this.getCardNumber());
        card.setCardDescription(this.getCardDescription());
        card.setEffects(new ArrayList<>());
    }

    public abstract Card cloneCard();

    public abstract void showCard();

    private void manageMonsterEffects() {

        switch (cardName) {
            case "Command Knight":
                effects.add(new AddAttackAndDefenseEffect(400, 400, MonsterCardType.ALL, 0));
                effects.add(new DisableAttackerEffect(2, 10));
                break;
            case "Yomi Ship":
                effects.add(new DestroyAttackerEffect());
                break;
            case "Suijin":
                effects.add(new SuijinEffect());
                break;
            case "Crab Turtle":
            case "Skull Guardian":
                break;
            case "Man-Eater Bug":
                effects.add(new ManEaterBugEffect());
                break;
            case "Gate Guardian":
                // not written yet
                break;
            case "Scanner":
                effects.add(new ScannerEffect());
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
                effects.add(new HeraldOfCreationEffect());
                break;
            case "Exploder Dragon":
                effects.add(new DenyLifePointChangeEffect());
                effects.add(new DestroyAttackerEffect());
                break;
            case "Terratiger the Empowered Warrior":
                effects.add(new SummonDefensePositionMonsterEffect());
                break;
            case "The Tricky":
                // not written yet
                break;
        }
    }

    private void manageSpellAndTrapEffects() {
        switch (cardName) {
            case "Monster Reborn":
                effects.add(new SummonFromGraveyardEffect());
                break;
            case "Terraforming":
                effects.add(new DrawEffect(1, SpellType.FIELD));
                break;
            case "Pot of Greed":
                effects.add(new DrawEffect(2, null));
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
                effects.add(new SwordsOfRevealingLightEffect());
                break;
            case "Dark Hole":
                effects.add(new DestroyEnemyMonsterEffect(true));
                break;
            case "Supply Squad":
                effects.add(new SupplySquadEffect());
                break;
            case "Spell Absorption":
                effects.add(new SpellAbsorptionEffect());
                break;
            case "Messenger of peace":
                effects.add(new MessengerOfPeaceEffect());
                break;
            case "Twin Twisters":
                effects.add(new TwinTwistersEffect());
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
                effects.add(new RitualSummonEffect());
                break;
            case "Magic Cylinder":
                effects.add(new MagicCylinderEffect());
                break;
            case "Mirror Force":
                effects.add(new MirrorForceEffect());
                break;
            case "Mind Crush":
                effects.add(new MindCrushEffect());
                break;
            case "Trap Hole":
                effects.add(new TrapHoleEffect());
                break;
            case "Torrential Tribute":
                effects.add(new TorrentialTributeEffect());
                break;
            case "Time Seal":
                effects.add(new DenyDrawCardEffect());
                break;
            case "Negate Attack":
                effects.add(new NegateAttackEffect());
                break;
            case "Solemn Warning":
                effects.add(new SolemnWarningEffect());
                break;
            case "Magic Jammer":
                effects.add(new MagicJammerEffect());
                break;
            case "Call of The Haunted":
                break;
        }
    }

    public void manageEffect() {
        // NOTE : should be called when creating players not at the beginning
        manageMonsterEffects();
        manageSpellAndTrapEffects();
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    @Override
    public int compareTo(Card o) {
        return this.cardName.compareTo(o.cardName);
    }
}
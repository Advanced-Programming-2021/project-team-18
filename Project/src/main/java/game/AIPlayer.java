package game;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import card.TrapCard;
import data.Printer;
import events.*;
import utility.Utility;

import java.util.ArrayList;

public class AIPlayer extends Player {

    private final ArrayList<Integer> indexes;
    private static Deck AIDeck;

    public static void setAIDeck(Deck AIDeck) {
        AIPlayer.AIDeck = AIDeck;
    }

    public static Deck getAIDeck() {
        return AIDeck;
    }

    public AIPlayer() {
        super(User.getDummyUser(), AIDeck);
        indexes = new ArrayList<>();
    }

    private Card getARandomElement(Card[] cardList) {
        indexes.clear();
        for (int i = 0; i < cardList.length; i++) {
            if (cardList[i] != null) indexes.add(i);
        }
        if (indexes.isEmpty()) return null;
        return cardList[Utility.getARandomNumber(indexes.size())];
    }

    private Card getARandomElement(Deck deck) {
        return deck.getCardsList().get(Utility.getARandomNumber(deck.getSize()));
    }

    @Override
    public boolean obtainConfirmation(String promptMassage) {
        if (promptMassage.matches(".*[aA]nswer.*")) return false;
        return Utility.getARandomNumber(2) == 1;
    }

    @Override
    public Card obtainSpellTrapFromField() {
        return getARandomElement(spellsAndTrapFieldList);
    }

    @Override
    public Card obtainCardFromDeck(boolean showDeck) {
        return getARandomElement(remainingDeck);
    }

    @Override
    public Card obtainCardFromGraveYard() {
        return getARandomElement(graveyard);
    }

    @Override
    public Card obtainCardFromHand() {
        return getARandomElement(hand);
    }

    @Override
    public int obtainNumberInRange(int l, int r, String prompt) {
        if (r <= l) return 0 / 0;
        return l + Utility.getARandomNumber(r - l);
    }

    // Activates a non-active spell/trap
    private void activateASpellOrTrap() {
        for (Card card : spellsAndTrapFieldList) {
            if (card == null) continue;
            if (!card.isFaceUp()) {
                selectedCard = card;
                activateEffect();
                return;
            }
        }
    }

    @Override
    // Sets a new spell or trap
    public boolean setSpellOrTrap() {
        int index = getFirstEmptyPlaceOnSpellsField();
        if (index == -1) return false;
        for (Card card : hand.getCardsList()) {
            if (card instanceof SpellCard || card instanceof TrapCard) {
                selectedCard = card;
                if (super.setSpellOrTrap()) return true;
            }
        }
        return false;
    }

    private int monstersOnField() {
        int cnt = 0;
        for (MonsterCard monsterCard : monstersFieldList) {
            if (monsterCard != null) cnt++;
        }
        return cnt;
    }

    private boolean summonOrSetMonster(MonsterCard monster, boolean set) {
        CardEvent event = new CardEvent(monster, CardEventInfo.ENTRANCE, null);
        if (!getPermissionFromAllEffects(event)) return false;
        int firstEmptyPlace = getFirstEmptyPlaceOnMonstersField();
        if (firstEmptyPlace == -1) return false;
        monster.setFaceUp(!set);
        monster.setDefenseMode(false);
        selectedCard = monster;
        int indexInHand = getSelectedCardOnHandID();
        if (monster.getCardLevel() <= 4) {
            summonMonsterLowLevel(indexInHand, firstEmptyPlace);
            notifyAllEffectsForConsideration(event);
            return true;
        }
        int monstersCount = monstersOnField();
        if (monster.getCardLevel() <= 6) {
            if (monstersCount < 1) return false;
            selectedCard = getARandomElement(monstersFieldList);
            summonMonsterMediumLevel(indexInHand, getSelectedMonsterCardOnFieldID());
            notifyAllEffectsForConsideration(event);
            return true;
        }
        if (monstersCount < 2) return false;
        MonsterCard tribute1 = (MonsterCard) getARandomElement(monstersFieldList);
        selectedCard = tribute1;
        int firstIndex = getSelectedMonsterCardOnFieldID();
        monstersFieldList[firstIndex] = null;
        selectedCard = getARandomElement(monstersFieldList);
        int secondIndex = getSelectedMonsterCardOnFieldID();
        monstersFieldList[firstIndex] = tribute1;
        summonMonsterHighLevel(firstIndex, secondIndex, indexInHand);
        notifyAllEffectsForConsideration(event);
        return true;
    }

    private void importNewMonster() {
        for (Card card : hand.getCardsList()) {
            if (card instanceof MonsterCard) {
                if (summonOrSetMonster((MonsterCard) card,
                        Utility.getARandomNumber(2) == 1)) return;
            }
        }
    }

    private MonsterCard selectBestMonsterForAttack() {
        MonsterCard mostPowerful = null;
        for (MonsterCard monsterCard : monstersFieldList) {
            if (monsterCard == null ||
                    (!monsterCard.isFaceUp()) ||
                    (monsterCard.isHasAttackedThisTurn()) ||
                    (monsterCard.isDefenseMode())) continue;
            if (mostPowerful == null ||
                    mostPowerful.getCardAttack() < monsterCard.getCardAttack())
                mostPowerful = monsterCard;
        }
        return mostPowerful;
    }

    // Searches is faced-up monsters of opponent
    private MonsterCard getWeakestMonsterOfOpponent() {
        int leastResistance = -1;
        MonsterCard weakestCard = null;
        for (MonsterCard monsterCard : opponent.getMonstersFieldList()) {
            if (monsterCard == null) continue;
            if (!monsterCard.isFaceUp()) continue;
            if (monsterCard.isDefenseMode()) {
                if (leastResistance == -1 ||
                        (monsterCard.getCardDefense() < leastResistance)) {
                    leastResistance = monsterCard.getCardDefense();
                    weakestCard = monsterCard;
                }
                continue;
            }
            if (leastResistance == -1 ||
                    (monsterCard.getCardAttack() < leastResistance)) {
                leastResistance = monsterCard.getCardAttack();
                weakestCard = monsterCard;
            }
        }
        return weakestCard;
    }

    private MonsterCard randomFacedDownMonsterOfOpponent() {
        ArrayList<MonsterCard> facedDowns = new ArrayList<>();
        for (MonsterCard monsterCard : opponent.getMonstersFieldList()) {
            if (!monsterCard.isFaceUp()) facedDowns.add(monsterCard);
        }
        if (facedDowns.isEmpty()) return null;
        return facedDowns.get(Utility.getARandomNumber(facedDowns.size()));
    }

    @Override
    public void battlePhase() {
        Printer.forcePrompt("phase: battle phase");
        MonsterCard attacker, defender;
        AttackEvent attackEvent;
        while ((attacker = selectBestMonsterForAttack()) != null) {
            selectedCard = attacker;
            defender = getWeakestMonsterOfOpponent();
            if (defender == null) defender = randomFacedDownMonsterOfOpponent();
            attackEvent = new AttackEvent(attacker, defender);
            if (!getPermissionFromAllEffects(attackEvent)) {
                attacker.setHasAttackedThisTurn(true);
                continue;
            }
            notifyAllEffectsForConsideration(attackEvent);
            if (defender == null) attackDirect();
            else attacker.attackTo(defender, this);
        }
        notifyAllEffectsForConsideration(new PhaseEndedEvent(Phase.BATTLE, this));
    }

    @Override
    public void mainPhase2() {
        Printer.forcePrompt("phase: main phase 2");
        for (MonsterCard monsterCard : monstersFieldList) {
            if (monsterCard == null) continue;
            if (!monsterCard.isFaceUp()) continue;
            if (monsterCard.isHasChangedPositionThisTurn()) continue;
            if (Utility.getARandomNumber(2) == 1) {
                monsterCard.setDefenseMode(!monsterCard.isDefenseMode());
                monsterCard.setHasChangedPositionThisTurn(true);
            }
        }
        notifyAllEffectsForConsideration(new PhaseEndedEvent(Phase.MAIN2, this));
    }

    @Override
    public void mainPhase1() {
        Printer.forcePrompt("phase: main phase 1");
        activateASpellOrTrap();
        setSpellOrTrap();
        importNewMonster();
        notifyAllEffectsForConsideration(new PhaseEndedEvent(Phase.MAIN1, this));
        // NOTE: FOR SIMPLICITY WE DO NOT PUT RITUAL MONSTERS AND RITUAL SPELLS IN OUR DECK!
        /*
        DRAW PHASE
        STANDBY PHASE
        MAIN PHASE 1
        1. Activate a spell/trap (if there is any inactive one)
        2. Set a spell/trap (if there is enough space and one exists in hand)
        3. Import a new monster (if there is enough space and one exists in hand)
        * Randomly decide to set it or summon it
        * If the monster needs tributes, check for existence of them
        BATTLE PHASE
        1. Select the most powerful monster that has not attacked this turn
        2. If there is a weaker faced-up monster, attack to it
        * Otherwise, attack to a random face-down monster, or attack directly if possible
        3. If there is no monster in attack position that has not attacked: go to next phase
        MAIN PHASE 2
        1. Set monsters in the position in which they are more powerful
        END PHASE


        ** Deck Selection!
         */
    }
}
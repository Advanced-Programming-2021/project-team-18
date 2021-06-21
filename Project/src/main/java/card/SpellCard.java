package card;

import data.Printer;
import effects.Effect;
import events.Event;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class SpellCard extends Card{
    private SpellType cardSpellType;

    @Override
    public void showCard() {
        String result = "Name: " + this.getCardName() + "\n";
        result += "Spell\n";
        result += "Type: " + this.getCardSpellType() + "\n";
        result += "Description " + this.getCardDescription() + "\n";
        Printer.prompt(result);
    }

    @Override
    public Card cloneCard() {
        SpellCard card = new SpellCard();
        this.cloneDefaults(card);
        card.setCardSpellType(this.getCardSpellType());
        return card;
    }
}

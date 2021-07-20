package card;

import com.google.gson.annotations.Expose;
import data.Printer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpellCard extends Card {
    @Expose
    private SpellType cardSpellType;
    public SpellCard() {

    }

    @Override
    public Card cloneCard() {
        SpellCard card = new SpellCard();
        this.cloneDefaults(card);
        card.setCardSpellType(this.getCardSpellType());
        return card;
    }
}

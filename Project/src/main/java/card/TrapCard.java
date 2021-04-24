package card;

import data.Printer;
import effects.Effect;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class TrapCard extends Card{
    private TrapType cardTrapType;
    private ArrayList<Effect> onActivationEffects;
    @Override
    public void showCard() {
        String result = "Name: " + this.getCardName() + "\n";
        result += "Trap\n";
        result += "Type: " + this.getCardTrapType() + "\n";
        result += "Description " + this.getCardDescription() + "\n";
        Printer.prompt(result);
    }
}

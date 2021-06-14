package menus;

import card.Card;
import com.google.gson.Gson;
import data.Printer;
import lombok.SneakyThrows;
import utility.Utility;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;

// by Pasha
public class ImportExportMenu extends Menu {
    private static final String PATH ="src/main/resources/cards";
    @SneakyThrows
    private void importCard(Matcher matcher) {
        String cardName = matcher.group(1);
        Gson gson = new Gson();
        String text = new String(Files.readAllBytes(Paths.get(PATH + cardName + ".json")));
        Card card = gson.fromJson(text , Card.class);
        Card.getAllCards().add(card);
    }
    @SneakyThrows
    private void exportCard(Matcher matcher) {
        String cardName = matcher.group(1);
        Card card = Card.getCardByName(cardName);
        if(card == null) {
            Printer.prompt("no card exists with name " + cardName + " to export");
            return ;
        }
        card.setPlayer(null);
        FileWriter fileWriter = new FileWriter(PATH + cardName + ".json");
        Gson gson = new Gson();
        String json = gson.toJson(card);
        fileWriter.write(json);
        fileWriter.close();
    }

    @Override
    public void runMenu() {
        String regexMenuExit = "menu\\sexit";
        String regexShowCurrentMenu = "menu\\sshow\\-current";
        String regexImportCard = "import\\scard\\s(.+)";
        String regexExportCard = "export\\scard\\s(.+)";
        Matcher matcher;
        while(true) {
            String newLine = Utility.getNextLine();
            if(Utility.getCommandMatcher(newLine , regexMenuExit).matches()) {
                break ;
            } else if(Utility.getCommandMatcher(newLine , regexShowCurrentMenu).matches()) {
                Printer.prompt("import/export menu");
            } else if((matcher = Utility.getCommandMatcher(newLine , regexImportCard)).matches()) {
                importCard(matcher);
            } else if((matcher = Utility.getCommandMatcher(newLine , regexExportCard)).matches()) {
                exportCard(matcher);
            } else {
                Printer.prompt(INVALID_COMMAND);
            }
        }
    }
}

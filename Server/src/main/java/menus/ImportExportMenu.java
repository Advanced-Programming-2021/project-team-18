package menus;

import card.Card;
import com.google.gson.Gson;
import data.Printer;
import lombok.SneakyThrows;
import utility.Utility;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;

// by Pasha
public class ImportExportMenu extends Menu {
    private static final String PATH = "/cards";

    @SneakyThrows
    private void importCard(Matcher matcher) {
        String cardName = matcher.group(1);
        Gson gson = new Gson();
        File file = new File(getClass().getResource(PATH + cardName + ".json").toURI());
        String text = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Card card = gson.fromJson(text, Card.class);
        Card.getAllCards().add(card);
    }

    @SneakyThrows
    private void exportCard(Matcher matcher) {
        String cardName = matcher.group(1);
        Card card = Card.getCardByName(cardName);
        if (card == null) {
            Printer.prompt("no card exists with name " + cardName + " to export");
            return;
        }
        card.setPlayer(null);
        File file = new File(getClass().getResource(PATH + cardName + ".json").toURI());
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        Gson gson = new Gson();
        String json = gson.toJson(card);
        fileWriter.write(json);
        fileWriter.close();
    }

    @Override
    public void runMenu() {
        String regexMenuExit = "menu\\s+exit";
        String regexShowCurrentMenu = "menu\\s+show\\-current";
        String regexImportCard = "import\\s+card\\s+(.+)";
        String regexExportCard = "export\\s+card\\s+(.+)";
        Matcher matcher;
        while (true) {
            String newLine = Utility.getNextLine();
            if (Utility.getCommandMatcher(newLine, regexMenuExit).matches()) {
                break;
            } else if (Utility.getCommandMatcher(newLine, regexShowCurrentMenu).matches()) {
                Printer.prompt("import/export menu");
            } else if ((matcher = Utility.getCommandMatcher(newLine, regexImportCard)).matches()) {
                importCard(matcher);
            } else if ((matcher = Utility.getCommandMatcher(newLine, regexExportCard)).matches()) {
                exportCard(matcher);
            } else {
                Printer.prompt(INVALID_COMMAND);
            }
        }
    }
}

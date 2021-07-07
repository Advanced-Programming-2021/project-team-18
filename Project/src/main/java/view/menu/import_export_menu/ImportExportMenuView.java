package view.menu.import_export_menu;

import card.Card;
import com.google.gson.Gson;
import data.Printer;
import game.User;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;
import view.components.CardComponent;
import view.menu.mainmenu.MainMenuView;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.regex.Matcher;

public class ImportExportMenuView extends View implements Initializable {
    @Setter
    private static User currentUser;
    public CardComponent cardComponent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Card card : Card.getAllCards())
            cardComponent.addCard(card);

    }

    public void onExportButton(MouseEvent mouseEvent) {
        if(cardComponent.getSelectedCardName() == null || cardComponent.getSelectedCardName().length() < 1) {
            UtilityView.displayMessage("no card was selected to export");
            return ;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);
        if(file == null) {
            UtilityView.displayMessage("invalid directory");
            return ;
        }
        exportCard(file.getAbsolutePath() , cardComponent.getSelectedCardName());
    }

    public void onImportButton(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json files" , ".json"));
        File file = fileChooser.showOpenDialog(null);
        if(file == null) {
            UtilityView.displayMessage("invalid directory");
            return ;
        }
        importCard(file.getAbsolutePath());
    }

    @SneakyThrows
    public void onBackButton(MouseEvent mouseEvent) {
        loadView("main_menu");
    }

    @SneakyThrows
    private void importCard(String path) {
        Gson gson = new Gson();
        File file = new File(path);
        String text = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Card card = gson.fromJson(text, Card.class);
        Card.getAllCards().add(card);
        UtilityView.displayMessage("card imported successfully");
    }

    @SneakyThrows
    private void exportCard(String path , String cardName) {
        Card card = Card.getCardByName(cardName);
        assert card != null;
        card.setEffects(null);
        card.setPlayer(null);
        File file = new File(path + "/" + cardName + ".json");
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        Gson gson = new Gson();
        String json = gson.toJson(card);
        fileWriter.write(json);
        fileWriter.close();
        UtilityView.displayMessage("card exported successfully");
    }
}

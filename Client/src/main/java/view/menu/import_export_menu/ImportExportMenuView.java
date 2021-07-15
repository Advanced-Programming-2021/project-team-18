package view.menu.import_export_menu;

import card.Card;
import card.MonsterCard;
import com.google.gson.Gson;
import game.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.SneakyThrows;
import menus.MenuController;
import view.UtilityView;
import view.View;
import view.components.CardComponent;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ImportExportMenuView extends View implements Initializable {
    public CardComponent cardComponent;
    public VBox buttonsBox;


    public static void setCurrentToken(String currentToken) {
        MenuController.getInstance().setToken(currentToken);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Card card : Card.getAllCards())
            cardComponent.addCard(card);

    }

    public void onExportButton() {
        if(cardComponent.getSelectedCardName() == null || cardComponent.getSelectedCardName().length() < 1) {
            UtilityView.showError("no card was selected to export");
            return ;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);
        if(file == null) {
            UtilityView.showError("invalid directory");
            return ;
        }
        exportCard(file.getAbsolutePath() , cardComponent.getSelectedCardName());
    }

    public void onImportButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json files" , "*.json"));
        File file = fileChooser.showOpenDialog(null);
        if(file == null) {
            UtilityView.showError("invalid directory");
            return ;
        }
        importCard(file.getAbsolutePath());
    }

    @SneakyThrows
    public void onBackButton() {
        loadView("main_menu");
    }

    @SneakyThrows
    private void importCard(String path) {
        Gson gson = new Gson();
        File file = new File(path);
        String text = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Card card = gson.fromJson(text, MonsterCard.class);
        Card.getAllCards().add(card);
        Card.getAllCardNames().add(card.getCardName());
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

    @Override
    public void adjustScene() {
        cardComponent.getImageView().fitWidthProperty().bind(stage.getScene().widthProperty().multiply(.2));
        cardComponent.getScrollPane().prefWidthProperty().bind(stage.getScene().widthProperty().multiply(.5));
        buttonsBox.minHeightProperty().bind(stage.getScene().heightProperty().multiply(.3));
    }
}

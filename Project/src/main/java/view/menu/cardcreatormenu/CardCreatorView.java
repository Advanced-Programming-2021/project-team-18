package view.menu.cardcreatormenu;

import card.Card;
import card.MonsterCard;
import card.MonsterCardType;
import game.User;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import view.UtilityView;
import view.View;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class CardCreatorView extends View implements Initializable {
    private static User currentUser;

    public TextField attackTextField;
    public TextField descriptionTextField;
    public ChoiceBox levelChoiceBox;
    public ChoiceBox monsterTypeChoiceBox;
    public Label priceText;
    public ImageView imageView;
    public TextField defenseTextField;
    public TextField nameTextField;

    public static void setCurrentUser(User currentUser) {
        CardCreatorView.currentUser = currentUser;
        MenuController.getInstance().setUser(currentUser);
    }

    @SneakyThrows
    public void onCreateClicked() {
        try {
            int attackValue = Integer.parseInt(attackTextField.getText());
            int defenseValue = Integer.parseInt(defenseTextField.getText());
            int price = getPrice();
            if(descriptionTextField.getText().length() < 1)
                throw new Exception("invalid options");
            if(nameTextField.getText().length() < 1)
                throw new Exception("invalid options");
            MonsterCard monsterCard = new MonsterCard();
            monsterCard.setCardName(nameTextField.getText());
            monsterCard.setCardAttack(attackValue);
            monsterCard.setCardDefense(defenseValue);
            monsterCard.setCardLevel((Integer) levelChoiceBox.getValue());
            monsterCard.setMonsterType((MonsterCardType) monsterTypeChoiceBox.getValue());
            monsterCard.setPrice(price);
            currentUser.setBalance(currentUser.getBalance() - price / 10);
            Card.getAllCards().add(monsterCard);
//            Card.getAllCardNames().add(monsterCard.getCardName());
        } catch (Exception e) {
            UtilityView.showError("invalid options");
            return ;
        }
        UtilityView.displayMessage("card successfully constructed and it cost you 10%");
        loadView("main_menu");
    }

    @SneakyThrows
    public void onBackClicked() {
        loadView("main_menu");
    }
    private int getPrice() {
        int price;
        try {
            price = 2 * Integer.parseInt(attackTextField.getText()) + Integer.parseInt(defenseTextField.getText()) + descriptionTextField.getText().length() * 100;
        } catch (Exception e) {
            price = 100;
        }
        return price;
    }
    private void refreshPrice() {
        priceText.setText(getPrice() + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        attackTextField.setText("100");
        defenseTextField.setText("100");
        bindTextField(attackTextField);
        bindTextField(defenseTextField);
        bindTextField(descriptionTextField);
        refreshPrice();
        Set<Integer> levels = new HashSet<>();
        Set<MonsterCardType> monsterCardTypes = new HashSet<>();
        for (Card card : Card.getAllCards())
            if (card instanceof MonsterCard) {
                levels.add(((MonsterCard) card).getCardLevel());
                monsterCardTypes.add(((MonsterCard) card).getMonsterType());
            }
        for (Integer level : levels)
            levelChoiceBox.getItems().add(level);
        levelChoiceBox.setValue(1);
        for (MonsterCardType monsterCardType : monsterCardTypes)
            monsterTypeChoiceBox.getItems().add(monsterCardType);
        monsterTypeChoiceBox.setValue(MonsterCardType.BEAST);
    }

    @Override
    public void adjustScene() {
        imageView.setPreserveRatio(true);
        final double imageWidth = imageView.getImage().getWidth();
        final double imageHeight = imageView.getImage().getHeight();
        imageView.fitWidthProperty().bind(Bindings.min(stage.getScene().widthProperty().multiply(.2),
                stage.getScene().heightProperty().multiply(imageWidth/imageHeight * .4)));
    }

    private void bindTextField(TextField textField) {
        textField.textProperty().addListener((observableValue, s, t1) -> refreshPrice());
    }
}

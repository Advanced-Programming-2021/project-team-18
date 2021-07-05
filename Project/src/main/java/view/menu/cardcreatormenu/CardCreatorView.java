package view.menu.cardcreatormenu;

import card.Card;
import card.MonsterCard;
import card.MonsterCardType;
import game.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class CardCreatorView extends View implements Initializable {
    @Setter
    private static User currentUser;

    public TextField attackTextField;
    public TextField descriptionTextField;
    public ChoiceBox levelChoiceBox;
    public ChoiceBox monsterTypeChoiceBox;
    public Text priceText;
    public ImageView imageView;
    public TextField defenseTextField;
    public TextField nameTextField;

    @SneakyThrows
    public void onCreateClicked(MouseEvent mouseEvent) {
        try {
            int attackValue = Integer.valueOf(attackTextField.getText());
            int defenseValue = Integer.valueOf(defenseTextField.getText());
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
            Card.getAllCardNames().add(monsterCard.getCardName());
        } catch (Exception e) {
            UtilityView.displayMessage("invalid options");
            return ;
        }
        UtilityView.displayMessage("card successfully constructed and it cost you 10%");
        loadView("main_menu");
    }

    @SneakyThrows
    public void onBackClicked(MouseEvent mouseEvent) {
        loadView("main_menu");
    }
    private int getPrice() {
        int price;
        try {
            price = 2 * Integer.valueOf(attackTextField.getText()) + Integer.valueOf(defenseTextField.getText()) + descriptionTextField.getText().length() * 100;
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

    private void bindTextField(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                refreshPrice();
            }
        });
    }
}
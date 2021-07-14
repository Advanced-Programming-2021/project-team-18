package view.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtainInformationListController {
    private String result;
    @FXML
    private Label label;
    @FXML
    private ListView listView;
    @FXML
    private Button selectButton;

}

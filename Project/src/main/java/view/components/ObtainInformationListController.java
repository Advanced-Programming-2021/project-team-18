package view.components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
@Setter
public class ObtainInformationListController {
    private String result;
    @FXML
    private ListView listView;
    @FXML
    private Button selectButton;

}

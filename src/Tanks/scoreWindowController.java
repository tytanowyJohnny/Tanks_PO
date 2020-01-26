package Tanks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;



import java.net.URL;
import java.util.ResourceBundle;

public class scoreWindowController implements Initializable {

    private ObservableList<String> items = FXCollections.observableArrayList ();

    @FXML
    private ListView<String> scoreListView;

    @FXML
    private Button scoreButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        scoreListView.setItems(items);

    }

    @FXML
    private void handleScoreButton(ActionEvent actionEvent) {

        // Get X & Y
        Stage mainWindow = (Stage) scoreButton.getScene().getWindow();
        // Close previous Window
        mainWindow.close();
    }


    public void addScore(String playerName, int score) {

        items.add(playerName + " - Score: " + score);
    }


}

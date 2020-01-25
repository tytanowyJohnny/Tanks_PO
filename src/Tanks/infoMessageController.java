package Tanks;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class infoMessageController {

    @FXML
    private Button okButton;

    @FXML
    private Label infoLabel;

    @FXML
    private void handleOKButton(ActionEvent actionEvent) {

        // Close window
        Stage currentWindow = (Stage) okButton.getScene().getWindow();
        currentWindow.close();

    }

    public void setInfoLabelText(String text) {

        infoLabel.setText(text);

    }

}

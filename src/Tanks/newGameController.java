package Tanks;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class newGameController implements Initializable {

    @FXML
    private Button newGameButton;

    @FXML
    private BorderPane imageTankBorder01;

    @FXML
    private BorderPane imageTankBorder02;

    @FXML
    private BorderPane imageTankBorder03;

    @FXML
    private BorderPane imageTankBorder04;

    @FXML
    private TextField usernameInput;

    @FXML
    private TextField roomInput;

    @FXML
    private TextField passwordInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        imageTankBorder01.getStyleClass().add("image-view-wrapper");
        imageTankBorder02.getStyleClass().add("image-view-wrapper");
        imageTankBorder03.getStyleClass().add("image-view-wrapper");
        imageTankBorder04.getStyleClass().add("image-view-wrapper");

    }

    @FXML
    protected void handleStartButton(ActionEvent ae) {

        try {

            if(Main.userSelectedTank == -1) {
                showInfoMessage("Musisz wybrać czołg!");
            } else if(usernameInput.getText().isEmpty()) {
                showInfoMessage("Musisz wpisać swój nickname!");
            } else {

                // Get X & Y
                Stage mainWindow = (Stage) newGameButton.getScene().getWindow();

                double x = mainWindow.getX();
                double y = mainWindow.getY();

                // Close previous Window
                mainWindow.close();

                // Open a new one
                FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyWindow.fxml"));
                SplitPane pane = loader.load();

                Main.lobbyWindowController = loader.getController();

                Main.lobbyWindowController.addPlayer(usernameInput.getText(), Main.isHost, Main.userSelectedTank);

                Scene newGameScene = new Scene(pane, 800, 550);
                Stage newGameStage = new Stage();

                newGameStage.setTitle("Tanks - Lobby");
                //primaryStage.setFullScreen(true);
                newGameStage.setScene(newGameScene);
                newGameStage.setX(x);
                newGameStage.setY(y);
                newGameStage.show();
            }

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    private void showInfoMessage(String text) {

        try {
            // Open a new one
            FXMLLoader loader = new FXMLLoader(getClass().getResource("infoMessage.fxml"));
            AnchorPane pane = loader.load();

            infoMessageController infoMessageController = loader.getController();

            infoMessageController.setInfoLabelText(text);

            Scene turnMessageScene = new Scene(pane);
            Stage turnMessageStage = new Stage();

            turnMessageStage.setTitle("It's time to make a move!");

            turnMessageStage.setScene(turnMessageScene);
            turnMessageStage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @FXML
    protected void chooseTank(MouseEvent ae) {

        boolean isSelected = false;
        ImageView selectedTank = (ImageView) ae.getSource();
        PseudoClass imageBorder = PseudoClass.getPseudoClass("border");

        // Clear previous selection
        imageTankBorder01.pseudoClassStateChanged(imageBorder, false);
        imageTankBorder02.pseudoClassStateChanged(imageBorder, false);
        imageTankBorder03.pseudoClassStateChanged(imageBorder, false);
        imageTankBorder04.pseudoClassStateChanged(imageBorder, false);

        // Save user selection in global variable
        Main.userSelectedTank = Integer.parseInt(selectedTank.getId().substring(selectedTank.getId().length()-1));

        // Enable border on new one
        switch (Main.userSelectedTank) {
            case 1:
                isSelected = imageTankBorder01.getPseudoClassStates().contains(imageBorder);
                imageTankBorder01.pseudoClassStateChanged(imageBorder, !isSelected);
                break;
            case 2:
                isSelected = imageTankBorder02.getPseudoClassStates().contains(imageBorder);
                imageTankBorder02.pseudoClassStateChanged(imageBorder, !isSelected);
                break;
            case 3:
                isSelected = imageTankBorder03.getPseudoClassStates().contains(imageBorder);
                imageTankBorder03.pseudoClassStateChanged(imageBorder, !isSelected);
                break;
            case 4:
                isSelected = imageTankBorder04.getPseudoClassStates().contains(imageBorder);
                imageTankBorder04.pseudoClassStateChanged(imageBorder, !isSelected);
                break;
            default:
                ///
                break;
        }

    }

}

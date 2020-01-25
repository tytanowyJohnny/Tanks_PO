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

            // Close previous Window
            Stage mainWindow = (Stage) newGameButton.getScene().getWindow();
            mainWindow.close();

            // Open a new one
            FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyWindow.fxml"));
            SplitPane pane = loader.load();

            lobbyWindowController lobbyWindowController = loader.getController();

            lobbyWindowController.addPlayer(usernameInput.getText(), Main.isHost, Main.userSelectedTank);

            Scene newGameScene = new Scene(pane, 800, 550);
            Stage newGameStage = new Stage();

            newGameStage.setTitle("Tanks - Lobby");
            //primaryStage.setFullScreen(true);
            newGameStage.setScene(newGameScene);
            newGameStage.show();

        } catch (IOException e) {

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

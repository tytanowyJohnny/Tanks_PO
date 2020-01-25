package Tanks;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class mainController {

    @FXML
    private Button exitGameButton;

    @FXML
    protected void handleNewGameButton(ActionEvent ae) {

        try {

            Main.isHost = 1;

            // Get X & Y
            Stage mainWindow = (Stage) exitGameButton.getScene().getWindow();

            double x = mainWindow.getX();
            double y = mainWindow.getY();

            // Close previous Window
            mainWindow.close();

            // Open a new one
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newGameWindow.fxml"));
            AnchorPane pane = loader.load();

            Scene newGameScene = new Scene(pane, 800, 550);
            newGameScene.getStylesheets().add("Tanks/css/style.css");
            Stage newGameStage = new Stage();

            newGameStage.setTitle("Tanks - Setup a new game!");
            //primaryStage.setFullScreen(true);
            newGameStage.setScene(newGameScene);
            newGameStage.setX(x);
            newGameStage.setY(y);
            newGameStage.show();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    @FXML
    protected void handleJoinGameButton(ActionEvent ae) {

        try {

            Main.isHost = 0;

            // Get X & Y
            Stage mainWindow = (Stage) exitGameButton.getScene().getWindow();

            double x = mainWindow.getX();
            double y = mainWindow.getY();

            // Close previous Window
            mainWindow.close();

            // Open a new one
            FXMLLoader loader = new FXMLLoader(getClass().getResource("joinGameWindow.fxml"));
            AnchorPane pane = loader.load();

            Scene newGameScene = new Scene(pane, 800, 550);
            newGameScene.getStylesheets().add("Tanks/css/style.css");
            Stage newGameStage = new Stage();

            newGameStage.setTitle("Tanks - Join existing game!");
            //primaryStage.setFullScreen(true);
            newGameStage.setScene(newGameScene);
            newGameStage.setX(x);
            newGameStage.setY(y);
            newGameStage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }


    }

    @FXML
    protected void handleExitGameButton(ActionEvent ae) {
        Platform.exit();
        System.exit(0);
    }

}
